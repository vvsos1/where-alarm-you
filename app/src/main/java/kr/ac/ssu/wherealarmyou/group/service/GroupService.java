package kr.ac.ssu.wherealarmyou.group.service;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Map;

import kr.ac.ssu.wherealarmyou.common.Icon;
import kr.ac.ssu.wherealarmyou.group.Group;
import kr.ac.ssu.wherealarmyou.group.GroupRepository;
import kr.ac.ssu.wherealarmyou.group.dto.GroupCreateRequest;
import kr.ac.ssu.wherealarmyou.group.dto.GroupModifyRequest;
import kr.ac.ssu.wherealarmyou.user.User;
import kr.ac.ssu.wherealarmyou.user.UserRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class GroupService {
    private static GroupService instance;

    private GroupRepository groupRepository;
    private UserRepository userRepository;

    public GroupService(GroupRepository groupRepository, UserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }

    public static GroupService getInstance() {
        if (instance == null)
            instance = new GroupService(GroupRepository.getInstance(), UserRepository.getInstance());
        return instance;
    }

    // 현재 로그인된 사용자가 가입한 그룹들을 리턴
    public Flux<Group> getJoinedGroup() {
//        return groupRepository.findGroupsByUserUid(userUid);
        String currentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        return userRepository.findUserByUid(currentUserUid)
                .map(User::getGroups)
                .flatMapIterable(Map::keySet)
                .flatMap(groupRepository::findGroupByUid);
    }

    public Mono<Group> createGroup(GroupCreateRequest request) {
        Group group = request.toGroup();
        return groupRepository.save(group);
    }

    public Mono<Group> modifyGroup(GroupModifyRequest request) {
        String currentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        String newName = request.getName();
        String newDescription = request.getDescription();
        Map<String, String> newMembers = request.getMembers();
        Icon newIcon = request.getIcon();


        String groupUid = request.getUid();
        return groupRepository.findGroupByUid(groupUid)
                .map(group -> {
                    if (!group.hasModifyAuthority(currentUserUid))
                        throw new IllegalArgumentException("권한이 없습니다");
                    group.modifyGroupInfo(newName, newDescription, newIcon, newMembers);
                    return group;
                })
                .flatMap(groupRepository::update);
    }

    public Mono<Group> findGroup(String groupUid) {
        return groupRepository.findGroupByUid(groupUid);
    }

    public Mono<Void> requestJoinGroup(String groupUid) {
        String currentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        return groupRepository.findGroupByUid(groupUid)
                .doOnNext(group -> group.requestJoin(currentUserUid))
                .flatMap(groupRepository::update)
                .then();
    }

    public Mono<Void> requestLeaveGroup(String groupUid) {
        String currentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        return groupRepository.findGroupByUid(groupUid)
                .doOnNext(group -> group.requestLeave(currentUserUid))
                .flatMap(groupRepository::update)
                .then();
    }

    public Mono<Void> deleteGroup(String groupUid) {
        String currentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        return groupRepository.findGroupByUid(groupUid)
                .map(group -> {
                    if (!group.hasGroupDeleteAuthority(currentUserUid))
                        throw new IllegalArgumentException("권한이 없습니다");
                    return group;
                })
                .flatMap(groupRepository::delete);
    }

    public Mono<Void> acceptWaitingUser(String groupUid, String userUid) {
        String currentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        return groupRepository.findGroupByUid(groupUid)
                .map(group -> {
                    // 현재 로그인중인 사용자가 해당 그룹에서 members 에 접근할 수 있는 권한이 있을 때만 실행
                    if (!group.hasMembersAuthority(currentUserUid))
                        throw new IllegalArgumentException("권한이 없습니다");
                    return group;
                })
                .doOnNext(group -> group.acceptWaitingUser(userUid))
                .flatMap(groupRepository::update)
                .then();
    }

    public Mono<Void> rejectWaitingUser(String groupUid, String userUid) {
        String currentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        return groupRepository.findGroupByUid(groupUid)
                .map(group -> {
                    // 현재 로그인중인 사용자가 해당 그룹에서 members 에 접근할 수 있는 권한이 있을 때만 실행
                    if (!group.hasMembersAuthority(currentUserUid))
                        throw new IllegalArgumentException("권한이 없습니다");
                    return group;
                })
                .doOnNext(group -> group.rejectWaitingUser(userUid))
                .flatMap(groupRepository::update)
                .then();
    }


}

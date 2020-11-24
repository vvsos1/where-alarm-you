package kr.ac.ssu.wherealarmyou.group.service;

import java.util.Map;

import kr.ac.ssu.wherealarmyou.group.Group;
import kr.ac.ssu.wherealarmyou.group.GroupRepository;
import kr.ac.ssu.wherealarmyou.group.dto.GroupCreateRequest;
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

    public Flux<Group> getJoinedGroup(String userUid) {
//        return groupRepository.findGroupsByUserUid(userUid);

        return userRepository.findUserByUid(userUid)
                .map(User::getGroups)
                .flatMapIterable(Map::keySet)
                .flatMap(groupRepository::findGroupByUid);
    }

    public Mono<Group> createGroup(GroupCreateRequest request) {
        Group group = request.toGroup();
        return groupRepository.save(group);
    }

    public Mono<Group> findGroup(String groupUid) {
        return groupRepository.findGroupByUid(groupUid);
    }

    public Mono<Void> requestJoinGroup(String groupUid, String userUid) {
        return groupRepository.findGroupByUid(groupUid)
                .doOnNext(group -> group.requestJoin(userUid))
                .flatMap(groupRepository::update);
    }

    public Mono<Void> acceptWaitingUser(String groupUid, String userUid) {
        return groupRepository.findGroupByUid(groupUid)
                .doOnNext(group -> group.acceptWaitingUser(userUid))
                .flatMap(groupRepository::update);
    }

    public Mono<Void> rejectWaitingUser(String groupUid, String userUid) {
        return groupRepository.findGroupByUid(groupUid)
                .doOnNext(group -> group.rejectWaitingUser(userUid))
                .flatMap(groupRepository::update);
    }


}

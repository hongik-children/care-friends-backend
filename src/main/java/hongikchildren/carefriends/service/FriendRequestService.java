package hongikchildren.carefriends.service;


import hongikchildren.carefriends.domain.Caregiver;
import hongikchildren.carefriends.domain.Friend;
import hongikchildren.carefriends.domain.FriendRequest;
import hongikchildren.carefriends.repository.CaregiverRepository;
import hongikchildren.carefriends.repository.FriendRepository;
import hongikchildren.carefriends.repository.FriendRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FriendRequestService {

    private final CaregiverRepository caregiverRepository;
    private final FriendRepository friendRepository;
    private final FriendRequestRepository friendRequestRepository;


    // 친구 요청 보내기
    @Transactional
    public void sendFriendRequest(Caregiver caregiver, UUID friendUUID){

        Optional<Friend> optionalFriend = friendRepository.findById(friendUUID);

        if (optionalFriend.isPresent()){
            Friend friend = optionalFriend.get();

            // 이미 보호자가 등록된 프렌드인 경우, 친구 요청을 보낼 수 없음
            if (friend.getCaregiver() != null){
                throw new RuntimeException("이미 보호자가 등록된 프렌드입니다.");
            }

            FriendRequest friendRequest = FriendRequest.builder()
                    .friend(friend)
                    .caregiver(caregiver)
                    .build();

            friendRequestRepository.save(friendRequest);
        } else{
            throw new RuntimeException("friend not found");
        }
    }


    // 친구 요청 수락하기
    @Transactional
    public void acceptFriendRequest(Long requestId){
        FriendRequest friendRequest = friendRequestRepository.findById(requestId)
                .orElseThrow(()-> new RuntimeException("Friend Request not found"));


        Caregiver caregiver = friendRequest.getCaregiver();
        caregiver.addFriend(friendRequest.getFriend());

        friendRequestRepository.delete(friendRequest);
        caregiverRepository.save(caregiver);

    }

    // 친구 요청 거절하기
    @Transactional
    public void rejectFriendRequest(Long requestId){
        FriendRequest friendRequest = friendRequestRepository.findById(requestId)
                .orElseThrow(()->new RuntimeException("Friend Request not found"));

        friendRequestRepository.delete(friendRequest);
    }


    // 대기중인 친구 요청 조회하기
    public List<FriendRequest> getPendingRequest(UUID friendId){
        Friend friend = friendRepository.findById(friendId)
                .orElseThrow(()->new RuntimeException("Friend not found"));

        return friendRequestRepository.findByFriend(friend);
    }
}
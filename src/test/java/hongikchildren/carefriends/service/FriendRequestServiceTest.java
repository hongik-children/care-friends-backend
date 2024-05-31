package hongikchildren.carefriends.service;

import hongikchildren.carefriends.domain.Caregiver;
import hongikchildren.carefriends.domain.Friend;
import hongikchildren.carefriends.domain.FriendRequest;
import hongikchildren.carefriends.domain.Gender;
import hongikchildren.carefriends.repository.CaregiverRepository;
import hongikchildren.carefriends.repository.FriendRepository;
import hongikchildren.carefriends.repository.FriendRequestRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class FriendRequestServiceTest {
    @Autowired
    CaregiverRepository caregiverRepository;

    @Autowired
    FriendRepository friendRepository;

    @Autowired
    FriendRequestRepository friendRequestRepository;

    @Autowired
    CaregiverService caregiverService;

    @Autowired
    FriendService friendService;

    @Autowired
    FriendRequestService friendRequestService;

    @Test
    public void testSendRequest(){
        // given
        Caregiver caregiver = caregiverService.saveCaregiver("혜윤", "010-7720-5751", Gender.FEMALE, LocalDate.of(2001, 11, 29));
        Friend friend1 = friendService.saveFriend("프렌드1", "010-9999-1223", Gender.MALE, LocalDate.of(1950, 11,11));

        // when
        friendRequestService.sendFriendRequest(caregiver, friend1.getId());
        FriendRequest friendRequest = friendRequestRepository.findAll().get(0);

        // then
        assertEquals(friendRequest.getFriend(), friend1);

    }

    @Test
    public void testAcceptRequest(){
        // given
        Caregiver caregiver1 = caregiverService.saveCaregiver("혜윤", "010-7720-5751", Gender.FEMALE, LocalDate.of(2001, 11, 29));
        Friend friend1 = friendService.saveFriend("프렌드1", "010-9999-1223", Gender.MALE, LocalDate.of(1950, 11,11));
        Friend friend2 = friendService.saveFriend("프렌드2", "010-9999-1223", Gender.MALE, LocalDate.of(1950, 11,11));
        friendRequestService.sendFriendRequest(caregiver1, friend1.getId());
        friendRequestService.sendFriendRequest(caregiver1, friend2.getId());
        FriendRequest request1 = friendRequestRepository.findByCaregiverAndFriend(caregiver1, friend1);
        FriendRequest request2 = friendRequestRepository.findByCaregiverAndFriend(caregiver1, friend2);


        // when
        friendRequestService.acceptFriendRequest(request1.getId());
        friendRequestService.acceptFriendRequest(request2.getId());


        // then
        assertEquals(caregiver1, friend1.getCaregiver());
        assertEquals(caregiver1, friend2.getCaregiver());
        assertEquals(0, friendRequestRepository.findByFriend(friend1).size());
        assertEquals(2, caregiver1.getFriends().size());
    }

    @Test
    public void testGetPendingRequest(){
        // given
        Caregiver caregiver1 = caregiverService.saveCaregiver("혜윤", "010-7720-5751", Gender.FEMALE, LocalDate.of(2001, 11, 29));
        Caregiver caregiver2 = caregiverService.saveCaregiver("보호자", "010-8342-5751", Gender.FEMALE, LocalDate.of(2001, 11, 29));
        Friend friend1 = friendService.saveFriend("프렌드1", "010-9999-1223", Gender.MALE, LocalDate.of(1950, 11,11));
        friendRequestService.sendFriendRequest(caregiver1, friend1.getId());
        friendRequestService.sendFriendRequest(caregiver2, friend1.getId());

        // when
        List<FriendRequest> friendRequests = friendRequestRepository.findByFriend(friend1);

        // then
        assertEquals(2, friendRequests.size());
    }
}

package hongikchildren.carefriends.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor
public class Caregiver {

    @Id @GeneratedValue
    @Column(name = "caregiverId")
    private Long id;

    private String name;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDate birthDate;

    @OneToMany(mappedBy = "caregiver")
    private List<Friends> friends =new ArrayList<>();


    @Builder
    protected Caregiver(Long id, String name, String phoneNumber, Gender gender, LocalDate birthDate) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.birthDate = birthDate;
    }


    // Friends 추가 메서드
    public void addFriends(Friends friends){
        this.friends.add(friends);
        friends.setCaregiver(this); // friends 엔티티의 caregiver 설정
    }

    // Friends 삭제 메서드
    public void removeFriends(Friends friends){
        this.friends.remove(friends);
        friends.removeCaregiver(); // friends 엔티티의 caregiver 초기화
    }

}

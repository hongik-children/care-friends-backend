package hongikchildren.carefriends.domain;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class Friends {

    @Id @GeneratedValue
    @Column(name = "friendsId")
    private Long id;

    private String name;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDate birthDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "caregiverId")
    private Caregiver caregiver;

    @Builder
    public Friends(Long id, String name, String phoneNumber, Gender gender, LocalDate birthDate) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.birthDate = birthDate;
    }

    // Caregiver 설정 메서드
    public void setCaregiver(Caregiver caregiver){
        this.caregiver = caregiver;
    }

    // Caregiver 초기화 메서드
    public void removeCaregiver(){
        this.caregiver = null;
    }


}

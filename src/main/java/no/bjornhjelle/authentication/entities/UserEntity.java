package no.bjornhjelle.authentication.entities;

import lombok.*;
import no.bjornhjelle.authentication.models.User;
import org.hibernate.annotations.Type;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity(name="Users")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper=false)
@Table(name = "users")
public class UserEntity extends EntityWithUUID {

    @NotNull(message = "First name is required.")
    @Basic(optional = false)
    @Column(name = "FIRST_NAME")
    private String firstName;

    @NotNull(message = "Last name is required.")
    @Basic(optional = false)
    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PHONE")
    private String phone;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "USER_STATUS")
    @Type(type = "no.bjornhjelle.authentication.entities.enumtypes.LabeledEnumType")
    private UserStatusEnum userStatus = UserStatusEnum.CREATED;

    @Column(name = "NICK_NAME")
    private String nickName;

    @Builder.Default
    @Column(name = "ROLE")
    @Enumerated(EnumType.STRING)
    @Type(type = "no.bjornhjelle.authentication.entities.enumtypes.LabeledEnumType")
    private RoleEnum role =  RoleEnum.USER;


    public User toUser(){
        User user = new User();
        user.setId(id.toString());
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPhone(phone);
        user.setCreatedAt(createdAt);
        user.setUpdatedAt(updatedAt);
        return user;
    }

    public String getUsername() {
        return email;
    }

}
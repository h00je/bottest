package com.bot.forksendr.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.telegram.telegrambots.meta.api.objects.Contact;

import java.io.Serializable;

/**
 * Данные анкеты пользователя
 */



@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(collection = "userProfileData")
public class UserProfileData implements Serializable {
    @Id
    String id;
    String name;
    String surname;
    String gender;
    String userName;
    int age;
    String contact;
    long chatId;



    public UserProfileData() {
    }

    protected boolean canEqual(final Object other) {
        return other instanceof UserProfileData;
    }


}
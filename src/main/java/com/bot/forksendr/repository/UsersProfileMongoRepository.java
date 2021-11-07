package com.bot.forksendr.repository;

import com.bot.forksendr.model.UserProfileData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.telegram.telegrambots.meta.api.objects.Contact;

@Repository
public interface UsersProfileMongoRepository extends MongoRepository <UserProfileData, String> {
    UserProfileData findByChatId(long chatId);
    
    void deleteByChatId(long chatId);
}

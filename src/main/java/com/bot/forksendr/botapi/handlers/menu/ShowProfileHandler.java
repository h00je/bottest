package com.bot.forksendr.botapi.handlers.menu;

import com.bot.forksendr.botapi.BotState;
import com.bot.forksendr.botapi.InputMessageHandler;
import com.bot.forksendr.model.UserProfileData;
import com.bot.forksendr.cache.UserDataCache;
import com.bot.forksendr.servise.UsersProfileDataService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class ShowProfileHandler implements InputMessageHandler {
    private UserDataCache userDataCache;
    private UsersProfileDataService profileDataService;

    public ShowProfileHandler(UserDataCache userDataCache, UsersProfileDataService profileDataService) {
        this.userDataCache = userDataCache;
        this.profileDataService = profileDataService;
    }

    @Override
    public SendMessage handle(Message message) {
        SendMessage userReply;
        final int userId = message.getFrom().getId();
        final UserProfileData profileData = profileDataService.getUserProfileData(message.getChatId());

        userDataCache.setUsersCurrentBotState(userId, BotState.SHOW_MAIN_MENU);
        if (profileData != null) {
            userReply = new SendMessage(message.getChatId(), String.format("%s%n -------------------%n" +
                    "Имя: %s%n" +
                    "Фамилия: %s%n" +
                    "Возраст: %d%n" +
                    "Пол: %s%n", "Данные по вашей анкете", profileData.getName(), profileData.getSurname(), profileData.getAge(), profileData.getGender()));
        } else {
            userReply = new SendMessage(message.getChatId(), "Такой анкеты в БД не существует!");
        }
        return userReply;
        }

//return new SendMessage(message.getChatId(), String.format("%s%n -------------------%nИмя: %s%nВозраст: %d%nПол: %s%nЛюбимая цифра: %d%n" +
//                                                                  "Цвет: %s%nФильм: %s%nПесня: %s%n", "Данные по вашей анкете", profileData.getName(), profileData.getAge(), profileData.getGender(), profileData.getNumber(),
//                profileData.getColor(), profileData.getMovie(), profileData.getSong()));

    @Override
    public BotState getHandlerName() {
        return BotState.SHOW_USER_PROFILE;
    }
}

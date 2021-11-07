package com.bot.forksendr.botapi.handlers.adminmenu;


import com.bot.forksendr.KsendrTelegramBot;
import com.bot.forksendr.botapi.BotState;
import com.bot.forksendr.botapi.InputMessageHandler;
import com.bot.forksendr.model.UserProfileData;
import com.bot.forksendr.servise.ReplyMessagesService;
import com.bot.forksendr.servise.UsersProfileDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

import java.io.FileNotFoundException;
import java.util.List;

@Slf4j
@Component
public class AdminMenu implements InputMessageHandler {

    private ReplyMessagesService messagesService;
    private KsendrTelegramBot ksendrTelegramBot;
    private UsersProfileDataService usersProfileDataService;

    public AdminMenu(ReplyMessagesService messagesService, @Lazy KsendrTelegramBot ksendrTelegramBot, UsersProfileDataService usersProfileDataService) {
        this.messagesService = messagesService;
        this.ksendrTelegramBot = ksendrTelegramBot;
        this.usersProfileDataService = usersProfileDataService;
    }

    @Override
    public SendMessage handle(Message message){
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() { return BotState.ADMIN_MENU;  }

    private SendMessage processUsersInput(Message inputMsg) {
        long chatId = inputMsg.getChatId();
        //SendMessage replyToUser = messagesService.getReplyMessage(chatId, "reply.badMessageFromMetodical");
       // usersProfileDataService.getAllProfiles();
        //List<UserProfileData> alluser = usersProfileDataService.getAllProfiles();

        StringBuilder sb = new StringBuilder("Все пользователи которые прошли анкетирование\r\n");
        List<UserProfileData> users = usersProfileDataService.getAllProfiles();

        users.forEach(user ->
                sb.append(user.getName())
                        .append(' ')
                        .append(user.getSurname())
                        .append(' ')
                        .append(user.getContact())
                        .append(' ')
                        .append("Логин телеграмм @" + user.getUserName())
                        .append(' ')
                        .append("\r\n")

        );
       SendMessage replyToUser = new SendMessage(inputMsg.getChatId(), sb.toString());

        return replyToUser;

    }
}

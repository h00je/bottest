package com.bot.forksendr.botapi.handlers.menu;

import com.bot.forksendr.KsendrTelegramBot;
import com.bot.forksendr.botapi.BotState;
import com.bot.forksendr.botapi.InputMessageHandler;
import com.bot.forksendr.servise.MainMenuService;
import com.bot.forksendr.servise.ReplyMessagesService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class ShowMyInfo implements InputMessageHandler {

    private ReplyMessagesService messagesService;
    private KsendrTelegramBot ksendrTelegramBot;

    public ShowMyInfo(ReplyMessagesService messagesService, @Lazy KsendrTelegramBot ksendrTelegramBot) {
        this.messagesService = messagesService;
        this.ksendrTelegramBot = ksendrTelegramBot;
    }

    @Override
    public SendMessage handle(Message message) {
        return processUsersInput(message);
    }
    private SendMessage processUsersInput(Message inputMsg) {
        long chatId = inputMsg.getChatId();

        ksendrTelegramBot.sendPhoto(chatId, messagesService.getReplyText("reply.showMyInfo"), "static/image/myinfo.jpg");
       SendMessage replyToUser = messagesService.getReplyMessage(chatId, "reply.showMyInfo2");

        return replyToUser;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.MY_INFO;
    }


}


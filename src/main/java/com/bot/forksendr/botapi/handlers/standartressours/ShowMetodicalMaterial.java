package com.bot.forksendr.botapi.handlers.standartressours;

import com.bot.forksendr.KsendrTelegramBot;
import com.bot.forksendr.botapi.BotState;
import com.bot.forksendr.botapi.InputMessageHandler;
import com.bot.forksendr.cache.UserDataCache;
import com.bot.forksendr.servise.ReplyMessagesService;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.K;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class ShowMetodicalMaterial implements InputMessageHandler {

    private ReplyMessagesService messagesService;
    private KsendrTelegramBot ksendrTelegramBot;


    public ShowMetodicalMaterial(ReplyMessagesService messagesService, @Lazy KsendrTelegramBot ksendrTelegramBot) {
        this.messagesService = messagesService;
        this.ksendrTelegramBot = ksendrTelegramBot;
    }

    @Override
    public SendMessage handle(Message message) throws FileNotFoundException {
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {return BotState.SHOW_METODICAL_MATERIAL;}

    private SendMessage processUsersInput(Message inputMsg) throws FileNotFoundException {
        long chatId = inputMsg.getChatId();
        if (inputMsg.getText().equals("Хочу методичку") || inputMsg.getText().equals("хочу методичку") || inputMsg.getText().equals("хочуметодичку"))  {

        ksendrTelegramBot.sendDocument(chatId, messagesService.getReplyText("reply.showMetodicalMaterial"), ResourceUtils.getFile("classpath:static/docs/Metodichka_po_pitaniyu.pdf") );
        SendMessage replyToUser = messagesService.getReplyMessage(chatId, "reply.showMetodicalMaterial1");
        replyToUser.setReplyMarkup(getMainMenuKeyboardStart());
        return replyToUser;


        }
        SendMessage replyToUser = messagesService.getReplyMessage(chatId, "reply.badMessageFromMetodical");
        replyToUser.setReplyMarkup(getMainMenuKeyboardStart());

        return replyToUser;
    }
    private ReplyKeyboardMarkup getMainMenuKeyboardStart() {

        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        //KeyboardRow row3 = new KeyboardRow();


        //row1.add(new KeyboardButton().setText("Nomer").setRequestContact(true));
        row1.add(new KeyboardButton("Результаты работы"));
        row1.add(new KeyboardButton("Рассказать о себе"));
        row2.add(new KeyboardButton("Контактные данные"));
        row2.add(new KeyboardButton("Моя анкета"));

        keyboard.add(row1);
        keyboard.add(row2);

        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

}

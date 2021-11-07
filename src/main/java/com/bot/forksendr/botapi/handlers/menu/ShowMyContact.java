package com.bot.forksendr.botapi.handlers.menu;

import com.bot.forksendr.botapi.BotState;
import com.bot.forksendr.botapi.InputMessageHandler;
import com.bot.forksendr.servise.MainMenuService;
import com.bot.forksendr.servise.ReplyMessagesService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class ShowMyContact implements InputMessageHandler{

    private ReplyMessagesService messagesService;
    public ShowMyContact(ReplyMessagesService messagesService) {
        this.messagesService = messagesService;
    }

    @Override
    public SendMessage handle(Message message) {
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.MY_CONTACT;
    }
    private SendMessage processUsersInput(Message inputMsg) {
        long chatId = inputMsg.getChatId();

        SendMessage replyToUser = messagesService.getReplyMessage(chatId, "reply.showMyContact");
        replyToUser.setReplyMarkup(getInlineMessageButtons());

        return replyToUser;
    }

    private InlineKeyboardMarkup getInlineMessageButtons() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonVkontakte = new InlineKeyboardButton().setText("Вконтакте");
        InlineKeyboardButton buttonTaplink = new InlineKeyboardButton().setText("Taplink");
        InlineKeyboardButton buttonWhtasApp = new InlineKeyboardButton().setText("WhatsApp");
        InlineKeyboardButton buttonTelegram = new InlineKeyboardButton().setText("Telegram");

        //Every button must have callBackData, or else not work !
        buttonVkontakte.setCallbackData("buttonVkontakte");
        buttonTaplink.setCallbackData("buttonTaplink");
        buttonWhtasApp.setCallbackData("buttonWhtasApp");
        buttonTelegram.setCallbackData("buttonTelegram");

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(buttonVkontakte);
        keyboardButtonsRow1.add(buttonTaplink);

        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        keyboardButtonsRow2.add(buttonWhtasApp);
        keyboardButtonsRow2.add(buttonTelegram);


        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }
}

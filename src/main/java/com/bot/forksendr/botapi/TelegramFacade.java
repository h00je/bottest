package com.bot.forksendr.botapi;

import com.bot.forksendr.KsendrTelegramBot;
import com.bot.forksendr.model.UserProfileData;
import com.bot.forksendr.botapi.handlers.menu.ShowResultOfWork;
import com.bot.forksendr.cache.UserDataCache;
import com.bot.forksendr.servise.MainMenuService;
import com.bot.forksendr.servise.ReplyMessagesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.FileNotFoundException;

@Component
@Slf4j
public class TelegramFacade {
    private BotStateContext botStateContext;
    private UserDataCache userDataCache;
    private MainMenuService mainMenuService;
    private KsendrTelegramBot ksendrTelegramBot;
    private ReplyMessagesService replyMessagesService;



    public TelegramFacade(BotStateContext botStateContext, UserDataCache userDataCache, MainMenuService mainMenuService, @Lazy KsendrTelegramBot ksendrTelegramBot,ReplyMessagesService replyMessagesService) {
        this.botStateContext = botStateContext;
        this.userDataCache = userDataCache;
        this.mainMenuService = mainMenuService;
        this.ksendrTelegramBot = ksendrTelegramBot;
        this.replyMessagesService = replyMessagesService;
    }

    public BotApiMethod<?> handleUpdate(Update update) throws FileNotFoundException {
        SendMessage replyMessage = null;

        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            log.info("New callbackQuery from User: {}, userId: {}, with data: {}", update.getCallbackQuery().getFrom().getUserName(),
                    callbackQuery.getFrom().getId(), update.getCallbackQuery().getData());
            return processCallbackQuery(callbackQuery);
        }


        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            log.info("New message from User:{}, userId: {}, chatId: {},  with text: {}",
                    message.getFrom().getUserName(), message.getFrom().getId(), message.getChatId(), message.getText());
            replyMessage = handleInputMessage(message);
        }

        return replyMessage;
    }

    private SendMessage handleInputMessage(Message message) throws FileNotFoundException {
        String inputMsg = message.getText();
        int userId = message.getFrom().getId();
        long chatId = message.getChatId();
        BotState botState;
        SendMessage replyMessage;

        switch (inputMsg) {
            case "/start":
                botState = BotState.START_MENU_BOT;
                ksendrTelegramBot.sendPhoto(chatId, replyMessagesService.getReplyText("reply.hello"), "static/image/start.jpg");
                break;
            case "Заполнить анкету":
                botState = BotState.FILLING_PROFILE;
                break;
            case "Моя анкета":
                botState = BotState.SHOW_USER_PROFILE;
                ksendrTelegramBot.sendDocument(chatId, replyMessagesService.getReplyText("reply.showMetodicalMaterial"), ResourceUtils.getFile("classpath:static/docs/Metodichka_po_pitaniyu.pdf") );
                break;
            case "Помощь":
                botState = BotState.SHOW_HELP_MENU;
                break;
            case "Контактные данные":
                botState = BotState.MY_CONTACT;
                break;
            case "Результаты работы":
                botState = BotState.SHOW_RESULT_OF_WORK;
                break;
            case "Рассказать о себе":
                botState = BotState.MY_INFO;
                break;
            default:
                botState = userDataCache.getUsersCurrentBotState(userId);
                break;
        }

        userDataCache.setUsersCurrentBotState(userId, botState);


        if (inputMsg.equals("ЗалупаКоня")) {
            replyMessage = botStateContext.processInputMessage(BotState.ADMIN_MENU, message);
        }
        else replyMessage = botStateContext.processInputMessage(botState, message);

        return replyMessage;
    }
    private BotApiMethod<?> processCallbackQuery(CallbackQuery buttonQuery) throws FileNotFoundException {
        final long chatId = buttonQuery.getMessage().getChatId();
        final int userId = buttonQuery.getFrom().getId();
        BotApiMethod<?> callBackAnswer = mainMenuService.getMainMenuMessage(chatId, "Воспользуйтесь главным меню");
        //BotApiMethod<?> callBackAnswer = new SendMessage(chatId, "Воспользуйтесь главным меню");


        //From Destiny choose buttons
        if (buttonQuery.getData().equals("buttonVkontakte")) {
            callBackAnswer = new SendMessage(chatId, "https://vk.com/igormaloletnev");
        } else if (buttonQuery.getData().equals("buttonTaplink")) {
            callBackAnswer = new SendMessage(chatId, "https://taplink.cc/igor.maloletnev");
        } else if (buttonQuery.getData().equals("buttonWhtasApp")) {
            callBackAnswer = new SendMessage(chatId, "https://api.whatsapp.com/send?phone=79991107793");
        } else if (buttonQuery.getData().equals("buttonTelegram")){
            callBackAnswer = new SendMessage(chatId, "@Igor_Maloletnev");
        }


        //From Gender choose buttons
        else if (buttonQuery.getData().equals("buttonMan")) {
            UserProfileData userProfileData = userDataCache.getUserProfileData(userId);
            userProfileData.setGender("Мужской");
            userDataCache.saveUserProfileData(userId, userProfileData);
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_CONTACT);
            callBackAnswer = new SendMessage(chatId, "Ведите Ваш номер телефона в формате '+79998887766'");
        } else if (buttonQuery.getData().equals("buttonWoman")) {
            UserProfileData userProfileData = userDataCache.getUserProfileData(userId);
            userProfileData.setGender("Женский");
            userDataCache.saveUserProfileData(userId, userProfileData);
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_CONTACT);
            callBackAnswer = new SendMessage(chatId, "Ведите Ваш номер телефона в формате '+79998887766'");
        }
      //From Result buttons

        else if (buttonQuery.getData().equals("buttonResult1")){
        ksendrTelegramBot.sendPhoto(chatId, replyMessagesService.getReplyText("reply.resultHistory1"), "static/image/result1.jpg");
        ksendrTelegramBot.sendPhoto(chatId, replyMessagesService.getReplyText("reply.resultHistory1"), "static/image/result1_2.jpg");
            callBackAnswer = new SendMessage(chatId, "Результат достигнут за 3 месяца");
        } else if (buttonQuery.getData().equals("buttonResult2")) {
            ksendrTelegramBot.sendPhoto(chatId, replyMessagesService.getReplyText("reply.resultHistory2"), "static/image/result2.jpg");
            ksendrTelegramBot.sendPhoto(chatId, replyMessagesService.getReplyText("reply.resultHistory2"), "static/image/result2_2.jpg");
            callBackAnswer = new SendMessage(chatId, "Результат достигнут за 1 месяц");
        } else if (buttonQuery.getData().equals("buttonResult3")) {
            ksendrTelegramBot.sendPhoto(chatId, replyMessagesService.getReplyText("reply.resultHistory3"), "static/image/result3.jpg");
            ksendrTelegramBot.sendPhoto(chatId, replyMessagesService.getReplyText("reply.resultHistory3"), "static/image/result3_2.jpg");
            callBackAnswer = new SendMessage(chatId, "Результат достигнут за 1 месяц");
        } else if (buttonQuery.getData().equals("buttonResult4")){
            ksendrTelegramBot.sendPhoto(chatId, replyMessagesService.getReplyText("reply.resultHistory4"), "static/image/result4.jpg");
            callBackAnswer = new SendMessage(chatId, "Результат достигнут за 3 месяца");


        }

       else {
            userDataCache.setUsersCurrentBotState(userId, BotState.SHOW_MAIN_MENU);

        }
        return callBackAnswer;


    }


//    private AnswerCallbackQuery sendAnswerCallbackQuery(String text, boolean alert, CallbackQuery callbackquery) {
//        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
//        answerCallbackQuery.setCallbackQueryId(callbackquery.getId());
//        answerCallbackQuery.setShowAlert(alert);
//        answerCallbackQuery.setText(text);
//        return answerCallbackQuery;
//    }


}

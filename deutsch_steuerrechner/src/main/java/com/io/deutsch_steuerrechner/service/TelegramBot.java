package com.io.deutsch_steuerrechner.service;

import com.io.deutsch_steuerrechner.config.BotConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static com.io.deutsch_steuerrechner.service.Variables.*;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    final BotConfig config;

    public TelegramBot(BotConfig config) throws FileNotFoundException {
        this.config = config;
        List<BotCommand> botCommands = new ArrayList<>();
        botCommands.add(new BotCommand("/start", "Bot starts to work."));
        botCommands.add(new BotCommand("/steuerrechnen", "To start to count tax."));
        botCommands.add(new BotCommand("/newcount", "new count."));
        botCommands.add(new BotCommand("/infotaxes", "info about taxes."));
        botCommands.add(new BotCommand("/help", "Information about bot."));
        try {
            this.execute(new SetMyCommands(botCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getBotUsername() {
        return config.botName;
    }

    @Override
    public String getBotToken() {
        return config.botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            switch (messageText) {
                case "/start":
                    startCommandRecived(chatId, update.getMessage().getChat().getFirstName());
                    break;
                case "/help":
                    sendMessege(chatId, COMMAND_HELP_TEXT);
                    break;
                default:
                    sendMessege(chatId, DEFAULT_UNKNOWN_COMMAND_MESSEGE);
            }
        }
    }

    private void startCommandRecived(long chatId, String usersName) {
        String answer = "Hallo, " + usersName + ". I'm Steuerrechner. I can help you to find out your tax group in Germany and how much money will you pay" +
                "from your brutto-salary for taxes, " +
                "and i will also calculate your netto-salary";
        sendMessege(chatId, answer);
    }
    private void sendMessege(long chatId, String textToSend){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        try {
            execute(message);
        }catch (TelegramApiException e){
            throw new RuntimeException(e);
        }

    }

    private void startToIndefyTaxGrou (long chatId, String messageText){
        usersFamily(chatId, messageText);
        usersChild(chatId, messageText);
        usersExtraWork(chatId, messageText);
        usersVisitsToChurch(chatId, messageText);
        usersMedicalInsurace(chatId, messageText);
        usersSalary(chatId, messageText);
        usersProfession(chatId, messageText);
    }

    private void usersFamily (long chatId, String messageText){
        sendMessege(chatId, QUESTION_ABOUT_FAMILY_STATUS);
        switch (messageText){
            case "Married":
                Boolean isMarried = true;
                sendMessege(chatId, QUESTION_ABOUT_SALARY_OF_PARTNER);
                switch (messageText){
                    case "More":
                        boolean usersSalaryMore = true;
                        boolean usersSalarySame = false;
                        break;

                    case "Less":
                        usersSalaryMore = false;
                        usersSalarySame = false;
                        break;

                    case "Same":
                        usersSalarySame = true;
                        usersSalaryMore = false;
                        break;
                }
                usersChild(chatId, messageText);
                break;


            case "Alone", "Diverse", "Windower":
                isMarried = false;
                usersChild(chatId, messageText);
                break;
        }
    }

    private void usersChild(long chatId, String messageText){
        sendMessege(chatId, QUESTION_ABOUT_CHILDREN);
        switch (messageText){
            case "Yes":
                boolean children = true;
                sendMessege(chatId, QUESTION_ABOUT_COUNT_OF_CHILDREN);
                switch (messageText){
                    case"1":
                        boolean countOfChildren_1 = true;
                        break;
                    case"2":
                        boolean countOfChildren_2 = true;
                        break;
                    case"3":
                        boolean countOfChildren_3 = true;
                        break;
                    case"4":
                        boolean countOfChildren_4 = true;
                        break;
                    case"5":
                        boolean countOfChildren_5 = true;
                        break;
                    case"6":
                        boolean countOfChildren_6 = true;
                        break;
                }
                break;
            case "No":
                children = false;
                break;
        }
    }

    private void usersExtraWork (long chatId, String messageText){
        sendMessege(chatId, QUESTION_ABOUT_MINIJOB);
        switch (messageText){
            case "Yes":
                boolean extraWork = true;
                sendMessege(chatId, QUESTION_ABOUT_SALARY_BY_MINIJOB);
                String salaryByMinijob = messageText;
                break;
            case "No":
                extraWork = false;
                break;
        }
    }
    private void usersVisitsToChurch(long chatId, String messageText){
        sendMessege(chatId, QUESTION_ABOUT_CHURCH);
        switch (messageText){
            case "Yes":
                boolean visitChurch = true;
                break;
            case "No":
                visitChurch = false;
                break;
        }
    }

    private void usersMedicalInsurace(long chatId, String messageText){
        sendMessege(chatId, QUESTION_ABOUT_MEDICAL_INSURACE);
        switch (messageText){
            case "private":
                boolean publicInsurace = false;
                break;
            case "public":
                publicInsurace = true;
                break;
        }
    }

    private void usersSalary(long chatId, String messageText){
        sendMessege(chatId, QUESTION_ABOUT_SALARY);
        String usersSalary = messageText;
    }
    private void usersProfession(long chatId, String messageText){
        sendMessege(chatId, QUESTION_ABOUT_PROFESSION);
        String usersProfession = messageText;
    }
    private void usersTaxGroup1(boolean isMarried, boolean children, long chatId){
    if((isMarried=false) && (children=false)){
        boolean usersTaxClass1 = true;
        sendMessege(chatId, TAX_CLASS1);
    }
    }
    private void usersTaxGroup2(boolean isMarried, boolean children, long chatId){
        if((isMarried=false) && (children=true)){
            boolean usersTaxClass2 = true;
            sendMessege(chatId, TAX_CLASS2);
        }
    }
    private void usersTaxGroup3(boolean isMarried, long chatId, boolean usersSalaryMore){
        if((isMarried=true) && (usersSalaryMore = true) ){
            boolean usersTaxClass3 = true;
            sendMessege(chatId, TAX_CLASS3);
        }
    }private void usersTaxGroup4(boolean isMarried, boolean usersSalarySame, long chatId){
        if((isMarried=true) && (usersSalarySame = true)){
            boolean usersTaxClass4 = true;
            sendMessege(chatId, TAX_CLASS4);
        }
    }
    private void usersTaxGroup5(boolean isMarried, boolean usersSalaryMore, long chatId){
        if((isMarried=true) && (usersSalaryMore=false)){
            boolean usersTaxClass5 = true;
            sendMessege(chatId, TAX_CLASS5);
        }
    }
    private void usersTaxGroup6(boolean extraWork, long chatId){
        if(extraWork = true){
            boolean usersTaxClass6 = true;
            sendMessege(chatId, TAX_CLASS6);
        }
    }



}



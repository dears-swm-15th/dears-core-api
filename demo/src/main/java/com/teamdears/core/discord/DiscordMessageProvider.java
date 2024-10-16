package com.teamdears.core.discord;

import com.teamdears.core.discord.event.DiscordFeignCustomerService;
import com.teamdears.core.discord.event.DiscordFeignException;
import com.teamdears.core.discord.message.CustomerServiceMessage;
import com.teamdears.core.discord.message.ExceptionMessage;
import com.teamdears.core.enums.member.MemberRole;
import com.teamdears.core.error.ErrorResponse;
import com.teamdears.core.error.UserInfo;
import com.teamdears.core.member.dto.MypageDTO;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DiscordMessageProvider {

    private final DiscordFeignCustomerService discordFeignCustomerService;
    private final DiscordFeignException discordFeignException;
    private final HttpServletRequest request;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    String formattedTimestamp = LocalDateTime.now().format(formatter);

    private static String exceptionToString(Exception ex) {
        StringWriter stringWriter = new StringWriter();
        ex.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

    private String getRequestPath() {
        return request.getRequestURI();
    }

    public void sendCustomerServiceMessage(String username, MemberRole role, String UUID, MypageDTO.CustomerServiceRequest request) {
        // Example embed creation
        CustomerServiceMessage.Embed embed = new CustomerServiceMessage.Embed(
                request.getTitle(),          // title
                request.getContent(),        // description
                4886754,                     // color (Cyan)
                new CustomerServiceMessage.Author(username),
                List.of(
                        new CustomerServiceMessage.Field("Role", role.getRoleName(), true),
                        new CustomerServiceMessage.Field("UUID", UUID, true)
                ),
                new CustomerServiceMessage.Footer(formattedTimestamp)
        );

        // Creating a Discord message with the embed
        CustomerServiceMessage customerServiceMessage = CustomerServiceMessage.createCustomerServiceMessage(
                EventMessage.CUSTOMER_SERVICE.getMessage(),
                List.of(embed)
        );

        sendMessageToDiscord(customerServiceMessage);
    }

    public void sendExceptionMessage(UserInfo userInfo, ErrorResponse response, Exception ex) {
        ExceptionMessage.Embed summary = new ExceptionMessage.Embed(
                response.getResultMsg(),
                response.getReason(),
                16711680,
                List.of(
                        new ExceptionMessage.Field("Status", String.valueOf(response.getStatus()), true),
                        new ExceptionMessage.Field("Division Code", response.getDivisionCode(), true),
                        new ExceptionMessage.Field("Request URL", getRequestPath(), true),
                        new ExceptionMessage.Field("Username", userInfo.username(), true),
                        new ExceptionMessage.Field("Role", userInfo.role().getRoleName(), true),
                        new ExceptionMessage.Field("UUID", userInfo.UUID(), true)
                ),
                new ExceptionMessage.Footer(formattedTimestamp)
        );

        ExceptionMessage.Embed detail = new ExceptionMessage.Embed(
                "Exception Details",
                exceptionToString(ex).substring(0, 2048),
                16711680,
                List.of(),
                new ExceptionMessage.Footer(formattedTimestamp)
        );

        ExceptionMessage customerServiceMessage = ExceptionMessage.createExceptionMessage(
                EventMessage.EXCEPTION.getMessage(),
                List.of(summary, detail)
        );

        sendMessageToDiscord(customerServiceMessage);
    }

    private void sendMessageToDiscord(CustomerServiceMessage customerServiceMessage) {
        try {
            discordFeignCustomerService.sendMessage(customerServiceMessage);
        } catch (FeignException e) {
            throw new FeignException.BadRequest(e.getMessage(), e.request(), e.request().body(), e.request().headers());
        }
    }

    private void sendMessageToDiscord(ExceptionMessage exceptionMessage) {
        try {
            discordFeignException.sendMessage(exceptionMessage);
        } catch (FeignException e) {
            throw new FeignException.BadRequest(e.getMessage(), e.request(), e.request().body(), e.request().headers());
        }
    }
}
package com.example.demo.discord;

import com.example.demo.discord.event.DiscordFeignCustomerService;
import com.example.demo.discord.event.DiscordFeignException;
import com.example.demo.discord.message.CustomerServiceMessage;
import com.example.demo.discord.message.ExceptionMessage;
import com.example.demo.enums.member.MemberRole;
import com.example.demo.error.ErrorResponse;
import com.example.demo.error.UserInfo;
import com.example.demo.member.dto.MypageDTO;
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
//                new DiscordMessage.Image("https://example.com/image.png"),
//                new DiscordMessage.Thumbnail("https://example.com/thumbnail.png"),
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

        sendExceptionMessageToDiscord(customerServiceMessage);
    }

    private void sendExceptionMessageToDiscord(CustomerServiceMessage customerServiceMessage) {
        try {
            discordFeignCustomerService.sendMessage(customerServiceMessage);
        } catch (FeignException e) {
            throw new FeignException.BadRequest(e.getMessage(), e.request(), e.request().body(), e.request().headers());
        }
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

        sendExceptionMessageToDiscord(customerServiceMessage);
    }

    private void sendExceptionMessageToDiscord(ExceptionMessage exceptionMessage) {
        try {
            discordFeignException.sendMessage(exceptionMessage);
        } catch (FeignException e) {
            throw new FeignException.BadRequest(e.getMessage(), e.request(), e.request().body(), e.request().headers());
        }
    }
}
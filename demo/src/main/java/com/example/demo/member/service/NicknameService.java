package com.example.demo.member.service;

import com.example.demo.enums.name.Adjective;
import com.example.demo.enums.name.Noun;
import com.example.demo.member.repository.CustomerRepository;
import com.example.demo.member.repository.WeddingPlannerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class NicknameService {

    private final CustomerRepository customerRepository;
    private final WeddingPlannerRepository weddingPlannerRepository;

    private final Random random = new Random();

    public String generateRandomNickname() {
        String nickname;

        do {
            Adjective adjective = Adjective.values()[random.nextInt(Adjective.values().length)];
            Noun noun = Noun.values()[random.nextInt(Noun.values().length)];
            int number = random.nextInt(1000); // 0~999
            nickname = adjective.name() + " " + noun.name() + " " + number;
        } while (customerRepository.existsByNickname(nickname) || weddingPlannerRepository.existsByNickname(nickname));

        return nickname;
    }

}

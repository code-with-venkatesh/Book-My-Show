package com.driver.bookMyShow.Services;

import com.driver.bookMyShow.Dtos.RequestDtos.UserEntryDto;
import com.driver.bookMyShow.Dtos.ResponseDtos.TicketResponseDto;
import com.driver.bookMyShow.Exceptions.UserAlreadyExistsWithEmail;
import com.driver.bookMyShow.Exceptions.UserDoesNotExists;
import com.driver.bookMyShow.Models.Ticket;
import com.driver.bookMyShow.Models.User;
import com.driver.bookMyShow.Repositories.UserRepository;
import com.driver.bookMyShow.Transformers.TicketTransformer;
import com.driver.bookMyShow.Transformers.UserTransformer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public String addUser(UserEntryDto userEntryDto) {
        if (userRepository.existsByEmailId(
                userEntryDto.getEmailId()
        )) {
            throw new UserAlreadyExistsWithEmail();
        }

        User user = UserTransformer.userDtoToUser(userEntryDto);
        userRepository.save(user);

        return "User saved successfully";
    }

    @Transactional(readOnly = true)
    public List<TicketResponseDto> allTickets(Integer userId) {
        User user = userRepository
                .findById(userId)
                .orElseThrow(UserDoesNotExists::new);

        List<TicketResponseDto> ticketResponses = new ArrayList<>();

        for (Ticket ticket : user.getTicketList()) {
            TicketResponseDto response =
                    TicketTransformer.returnTicket(
                            ticket.getShow(),
                            ticket
                    );

            ticketResponses.add(response);
        }

        return ticketResponses;
    }
}
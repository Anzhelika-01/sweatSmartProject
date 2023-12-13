package bg.softuni.sweatsmartproject.service;

import bg.softuni.sweatsmartproject.domain.dto.model.MessageModel;
import bg.softuni.sweatsmartproject.domain.entity.Message;
import bg.softuni.sweatsmartproject.repository.MessageRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
    private final MessageRepo messageRepo;

    private final ModelMapper modelMapper;

    @Autowired
    public MessageService(MessageRepo messageRepo, ModelMapper modelMapper) {
        this.messageRepo = messageRepo;
        this.modelMapper = modelMapper;
    }

    public void addMessage(MessageModel messageModel){

        final Message message = this.modelMapper.map(messageModel, Message.class);

        this.messageRepo.saveAndFlush(message);
    }
}
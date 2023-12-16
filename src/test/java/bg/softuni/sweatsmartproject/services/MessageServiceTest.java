package bg.softuni.sweatsmartproject.services;

import bg.softuni.sweatsmartproject.domain.dto.model.MessageModel;
import bg.softuni.sweatsmartproject.domain.entity.Message;
import bg.softuni.sweatsmartproject.repository.MessageRepo;
import bg.softuni.sweatsmartproject.service.MessageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class MessageServiceTest {

    @Mock
    private MessageRepo messageRepo;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private MessageService messageService;

    @Test
    void testAddMessage() {

        MessageModel messageModel = new MessageModel();
        Message mappedMessage = new Message();

        Mockito.when(modelMapper.map(messageModel, Message.class)).thenReturn(mappedMessage);

        messageService.addMessage(messageModel);

        verify(messageRepo).saveAndFlush(mappedMessage);
    }

}
package com.academy.springwebapplication.validator;

import com.academy.springwebapplication.dto.DepartureDto;
import com.academy.springwebapplication.model.entity.Route;
import com.academy.springwebapplication.model.entity.Train;
import com.academy.springwebapplication.model.repository.RouteRepository;
import com.academy.springwebapplication.model.repository.TrainRepository;
import com.academy.springwebapplication.util.TestObjectFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
class NewDepartureValidatorTest {

    @InjectMocks
    private NewDepartureValidator newDepartureValidator;

    @InjectMocks
    private TestObjectFactory testObjectFactory;

    @Mock
    private RouteRepository routeRepository;

    @Mock
    private TrainRepository trainRepository;

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder constraintViolationBuilder;

    @BeforeEach
    void init() {
        when(constraintValidatorContext.buildConstraintViolationWithTemplate(any())).thenReturn(constraintViolationBuilder);
        when(constraintViolationBuilder.addConstraintViolation()).thenReturn(constraintValidatorContext);
    }

    @Test
    void testWhenTrainOrRouteDoesNotExist() {
        DepartureDto departureDto = testObjectFactory.getDepartureDto();

        Optional<Train> optionalTrain = Optional.empty();
        when(trainRepository.findById(departureDto.getTrain().getId())).thenReturn(optionalTrain);

        Optional<Route> optionalRoute = Optional.empty();
        when(routeRepository.findById(departureDto.getRoute().getId())).thenReturn(optionalRoute);

        assertFalse(newDepartureValidator.isValid(departureDto, constraintValidatorContext));
    }

    @Test
    void testWhenDtoIsNotValid() {
        DepartureDto departureDto = testObjectFactory.getDepartureDto();

        Train train = new Train();
        train.setType("distant");

        Optional<Train> optionalTrain = Optional.of(train);
        when(trainRepository.findById(departureDto.getTrain().getId())).thenReturn(optionalTrain);

        Route route = new Route();
        route.setType("regional");

        Optional<Route> optionalRoute = Optional.of(route);
        when(routeRepository.findById(departureDto.getRoute().getId())).thenReturn(optionalRoute);

        assertFalse(newDepartureValidator.isValid(departureDto, constraintValidatorContext));
    }

    @Test
    void testWhenDtoIsValid() {
        DepartureDto departureDto = testObjectFactory.getDepartureDto();

        Train train = new Train();
        train.setType("distant");

        Optional<Train> optionalTrain = Optional.of(train);
        when(trainRepository.findById(departureDto.getTrain().getId())).thenReturn(optionalTrain);

        Route route = new Route();
        route.setType("international");

        Optional<Route> optionalRoute = Optional.of(route);
        when(routeRepository.findById(departureDto.getRoute().getId())).thenReturn(optionalRoute);

        assertTrue(newDepartureValidator.isValid(departureDto, constraintValidatorContext));
    }
}

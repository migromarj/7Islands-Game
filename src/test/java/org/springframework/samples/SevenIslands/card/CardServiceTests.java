package org.springframework.samples.SevenIslands.card;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class CardServiceTests {

    @Autowired
    private CardService cardService;
    
    @Test
    public void testCountWithInitialData(){
        int count = cardService.cardCount();
        assertEquals(count,4);
    }

    @Test
    public void testGetByPlayerId() {
        Iterable<Card> cards = cardService.getByPlayerId(1);
        assertEquals(cards.spliterator().getExactSizeIfKnown(), 3);
    }
    
}
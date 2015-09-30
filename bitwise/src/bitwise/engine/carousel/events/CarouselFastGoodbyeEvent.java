package bitwise.engine.carousel.events;

import bitwise.engine.carousel.Carousel;
import bitwise.engine.eventbus.Event;


public final class CarouselFastGoodbyeEvent extends Event {
	private final Carousel carousel;
	
	public CarouselFastGoodbyeEvent(Carousel in_carousel) {
		super("Carousel Fast Goodbye");
		carousel = in_carousel;
		assert(null != carousel);
	}
	
	@Override
	public String getDescription() {
		return String.format("Carousel [%s] shutting down. No new requests will be served; existing requests will be asked to abort.", carousel);
	}
}

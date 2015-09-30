package bitwise.engine.carousel.events;

import bitwise.engine.carousel.Carousel;
import bitwise.engine.eventbus.Event;


public final class CarouselHelloEvent extends Event {
	private final Carousel carousel;
	
	public CarouselHelloEvent(Carousel in_carousel) {
		super("Carousel Hello");
		carousel = in_carousel;
		assert(null != carousel);
	}

	@Override
	public String getDescription() {
		return String.format("Carousel [%s] started and ready to process new requests.", carousel);
	}
}

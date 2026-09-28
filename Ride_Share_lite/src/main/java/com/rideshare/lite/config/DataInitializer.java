package com.rideshare.lite.config;

import com.rideshare.lite.model.RideOffer;
import com.rideshare.lite.model.RideOfferStatus;
import com.rideshare.lite.model.RideRequest;
import com.rideshare.lite.model.RideRequestStatus;
import com.rideshare.lite.model.User;
import com.rideshare.lite.repository.RideOfferRepository;
import com.rideshare.lite.repository.RideRequestRepository;
import com.rideshare.lite.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final RideOfferRepository rideOfferRepository;
    private final RideRequestRepository rideRequestRepository;

    public DataInitializer(UserRepository userRepository,
                           RideOfferRepository rideOfferRepository,
                           RideRequestRepository rideRequestRepository) {
        this.userRepository = userRepository;
        this.rideOfferRepository = rideOfferRepository;
        this.rideRequestRepository = rideRequestRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Initializing RideShareLite sample data...");

        // 1. Create Users
        User driver1 = userRepository.save(new User(null, "Arun Kumar", "arun.kumar@college.edu", "+91 9876543210"));
        User driver2 = userRepository.save(new User(null, "Priya Sharma", "priya.sharma@techcorp.com", "+91 9876543211"));
        User rider1 = userRepository.save(new User(null, "Karthik Raja", "karthik.raja@college.edu", "+91 9876543212"));
        User rider2 = userRepository.save(new User(null, "Divya Nair", "divya.nair@techcorp.com", "+91 9876543213"));
        User rider3 = userRepository.save(new User(null, "Suresh V", "suresh.v@college.edu", "+91 9876543214"));

        log.info("Seeded 5 initial users.");

        // 2. Create Ride Offers
        LocalDateTime tomorrow8AM = LocalDateTime.now().plusDays(1).withHour(8).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime tomorrow9AM = LocalDateTime.now().plusDays(1).withHour(9).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime dayAfter5PM = LocalDateTime.now().plusDays(2).withHour(17).withMinute(30).withSecond(0).withNano(0);

        RideOffer offer1 = new RideOffer(driver1, "Gandhipuram, Coimbatore", "Sri Eshwar College, Kinathukadavu", tomorrow8AM, 3);
        RideOffer offer2 = new RideOffer(driver2, "T. Nagar, Chennai", "OMR Tech Park, Chennai", tomorrow9AM, 4);
        RideOffer offer3 = new RideOffer(driver1, "Sri Eshwar College", "Gandhipuram, Coimbatore", dayAfter5PM, 2);

        offer1 = rideOfferRepository.save(offer1);
        offer2 = rideOfferRepository.save(offer2);
        offer3 = rideOfferRepository.save(offer3);

        log.info("Seeded 3 initial ride offers.");

        // 3. Create Ride Requests
        RideRequest req1 = new RideRequest(offer1, rider1, 1);
        req1 = rideRequestRepository.save(req1);

        // Approve req1: seats decrease from 3 to 2
        offer1.setAvailableSeats(offer1.getAvailableSeats() - req1.getSeatsRequested());
        rideOfferRepository.save(offer1);
        req1.setStatus(RideRequestStatus.APPROVED);
        rideRequestRepository.save(req1);

        RideRequest req2 = new RideRequest(offer1, rider2, 1);
        rideRequestRepository.save(req2); // PENDING

        RideRequest req3 = new RideRequest(offer2, rider3, 2);
        rideRequestRepository.save(req3); // PENDING

        log.info("Seeded 3 sample ride requests (1 Approved, 2 Pending).");
        log.info("RideShareLite sample data initialization completed successfully!");
    }
}

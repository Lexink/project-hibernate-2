package com.javarush;

import com.javarush.dao.*;
import com.javarush.domain.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class Main {
    private final ActorDAO actorDAO;
    private final AddressDAO addressDAO;
    private final CategoryDAO categoryDAO;
    private final CityDAO cityDAO;
    private final CustomerDAO customerDAO;
    private final FilmDAO filmDAO;
    private final FilmTextDAO filmTextDAO;
    private final InventoryDAO inventoryDAO;
    private final LanguageDAO languageDAO;
    private final PaymentDAO paymentDAO;
    private final RentalDAO rentalDAO;
    private final StaffDAO staffDAO;
    private final StoreDAO storeDAO;

    public Main() {
        SessionFactory sessionFactory = MySessionFactory.getSessionFactory();
        actorDAO = new ActorDAO(sessionFactory);
        addressDAO = new AddressDAO(sessionFactory);
        categoryDAO = new CategoryDAO(sessionFactory);
        cityDAO = new CityDAO(sessionFactory);
        customerDAO = new CustomerDAO(sessionFactory);
        filmDAO = new FilmDAO(sessionFactory);
        filmTextDAO = new FilmTextDAO(sessionFactory);
        inventoryDAO = new InventoryDAO(sessionFactory);
        languageDAO = new LanguageDAO(sessionFactory);
        paymentDAO = new PaymentDAO(sessionFactory);
        rentalDAO = new RentalDAO(sessionFactory);
        staffDAO = new StaffDAO(sessionFactory);
        storeDAO = new StoreDAO(sessionFactory);
    }

    public static void main(String[] args) {

        Main main = new Main();
        Customer customer = main.createCustomer();
        main.returnInventoryByRandomCustomer();
        main.rentInventoryByCustomer(customer);
        Film film = main.createNewFilm("New My Film", "This is a good film");

    }

    public Customer createCustomer(){
        try (Session session = MySessionFactory.getSessionFactory().getCurrentSession()) {
            session.beginTransaction();

            Store store = storeDAO.getItems(0, 1).get(0);
            City city = cityDAO.getByName("Ipoh");
            Address address = new Address();
            address.setCity(city);
            address.setAddress("Pahim, 4-87");
            address.setDistrict("Rojak");
            address.setPhone("093-76-9-13");
            addressDAO.save(address);

            Customer customer = new Customer();
            customer.setFirstName("Pjoh");
            customer.setLastName("Omhra");
            customer.setEmail("p.omhra@krast.com");
            customer.setStore(store);
            customer.setIsActive(true);
            customer.setAddress(address);
            customerDAO.save(customer);

            session.getTransaction().commit();
            return customer;
        }
    }

    public void returnInventoryByRandomCustomer() {
        try (Session session = MySessionFactory.getSessionFactory().getCurrentSession()) {
            session.beginTransaction();
            Customer customer = customerDAO.getRandomCustomer();
            List<Rental> rentalList = rentalDAO.getRentalByCustomer(customer);
            Optional<Rental> rentalOpt = rentalList.stream().filter(r->r.getReturnDate() == null).findAny();
            if (rentalOpt.isPresent()){
                Rental rental = rentalOpt.get();
                rental.setReturnDate(LocalDateTime.now());
                rentalDAO.save(rental);
                System.out.printf("Арендатор %s %s вернул фильм %s", customer.getFirstName(), customer.getLastName(), rental.getReturnDate());
            } else {
                System.out.printf("У, случайно выбранного, арендатора %s %s нет невозвращенных фильмов", customer.getFirstName(), customer.getLastName());
            }

            session.getTransaction().commit();
        }
    }

    public void rentInventoryByCustomer(Customer customer){
        try (Session session = MySessionFactory.getSessionFactory().getCurrentSession()) {
            session.beginTransaction();
            Inventory inventory = inventoryDAO.getNotRentedInventory();
            Store store = inventory.getStore();
            Staff staff = staffDAO.getStaffByStore(store);
            Rental rental = new Rental();
            rental.setCustomer(customer);
            rental.setInventory(inventory);
            rental.setStaff(staff);
            rental.setRentalDate(LocalDateTime.now());
            rentalDAO.save(rental);
            Payment payment = new Payment();
            payment.setCustomer(customer);
            payment.setStaff(staff);
            payment.setRental(rental);
            payment.setAmount(BigDecimal.valueOf(8.06));
            paymentDAO.save(payment);

            System.out.printf("Арендатор %s %s взял в аренду фильм %s %s числа в магазине на сумму %f",
                    customer.getFirstName(),
                    customer.getLastName(),
                    inventory.getFilm().getTitle(),
                    rental.getRentalDate(),
                    payment.getAmount().doubleValue());

            session.getTransaction().commit();
        }
    }

    public Film createNewFilm(String title, String description) {
        try (Session session = MySessionFactory.getSessionFactory().getCurrentSession()) {
            session.beginTransaction();

            Language language = languageDAO.getItems(0, 30).stream().unordered().findAny().get();
            Set<Actor> actors = new HashSet<>(actorDAO.getItems(5, 28));
            Set<Category> categories = new HashSet<>(categoryDAO.getItems(2, 4));

            Film film = new Film();
            film.setTitle(title);
            film.setDescription(description);
            film.setReleaseYear(Year.now());
            film.setLanguage(language);
            film.setRentalDuration((byte) 20);
            film.setRentalRate(BigDecimal.ZERO);
            film.setLength((short) 180);
            film.setReplacementCost(BigDecimal.valueOf(200L));
            film.setRating(Rating.PG13);
            film.setSpecialFeatures(Set.of(SpecialFeature.TRAILERS, SpecialFeature.DELETED_SCENES));
            film.setActors(actors);
            film.setCategories(categories);

            filmDAO.save(film);

            FilmText filmText = new FilmText();
            filmText.setId(film.getId());
            filmText.setFilm(film);
            filmText.setTitle(title);
            filmText.setDescription(description);

            filmTextDAO.save(filmText);

            List<Store> stores = storeDAO.getAll();
            for (Store store : stores) {
                Inventory inventory = new Inventory();
                inventory.setFilm(film);
                inventory.setStore(store);
                inventoryDAO.save(inventory);
            }

            session.getTransaction().commit();
            return film;
        }
    }
}

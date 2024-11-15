
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Room {

    private int roomNumber;
    private String roomType;
    private double price;
    private boolean isAvailable;

    public Room(int roomNumber, String roomType, double price) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.price = price;
        this.isAvailable = true;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public double getPrice() {
        return price;
    }

    public String getRoomType() {
        return roomType;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    @Override
    public String toString() {
        return "Room " + roomNumber + " [" + roomType + ", $" + price + "/night]";
    }
}

class Reservation {

    private static int counter = 1;
    private int reservationId;
    private Room room;
    private User user;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private double totalCost;

    public Reservation(Room room, User user, LocalDate checkInDate, LocalDate checkOutDate) {
        this.reservationId = counter++;
        this.room = room;
        this.user = user;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.totalCost = calculateTotalCost();
        room.setAvailable(false); // Mark the room as reserved
    }

    public double calculateTotalCost() {
        int days = checkOutDate.getDayOfYear() - checkInDate.getDayOfYear();
        return days * room.getPrice();
    }

    public int getReservationId() {
        return reservationId;
    }

    @Override
    public String toString() {
        return "Reservation " + reservationId + ": " + room + ", User: " + user.getName() + ", Total Cost: $" + totalCost;
    }
}

class User {

    private String name;
    private String contactInfo;
    private List<Reservation> reservations;

    public User(String name, String contactInfo) {
        this.name = name;
        this.contactInfo = contactInfo;
        this.reservations = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
    }
}

class Hotel {

    private List<Room> rooms = new ArrayList<>();

    public void addRoom(Room room) {
        rooms.add(room);
    }

    public List<Room> searchAvailableRooms(String roomType) {
        List<Room> availableRooms = new ArrayList<>();
        for (Room room : rooms) {
            if (room.isAvailable() && room.getRoomType().equalsIgnoreCase(roomType)) {
                availableRooms.add(room);
            }
        }
        return availableRooms;
    }

    public Reservation makeReservation(User user, Room room, LocalDate checkIn, LocalDate checkOut) {
        Reservation reservation = new Reservation(room, user, checkIn, checkOut);
        user.addReservation(reservation);
        return reservation;
    }
}

class Payment {

    public boolean processPayment(double amount) {
        System.out.println("Processing payment of $" + amount + "...");
        return true; // Always returns success in this simulation
    }
}

public class Hotel_reservation_system {

    public static void main(String[] args) {
        Hotel hotel = new Hotel();
        Scanner scanner = new Scanner(System.in);

        // Adding some rooms to the hotel
        hotel.addRoom(new Room(101, "Standard", 100));
        hotel.addRoom(new Room(102, "Deluxe", 150));
        hotel.addRoom(new Room(103, "Suite", 200));

        System.out.println("Welcome to the Hotel Reservation System!");

        // User Information
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        System.out.print("Enter your contact information: ");
        String contactInfo = scanner.nextLine();
        User user = new User(name, contactInfo);

        // Room Search
        System.out.print("Enter room type to search (Standard/Deluxe/Suite): ");
        String roomType = scanner.nextLine();
        System.out.println("Available Rooms: ");
        for (Room room : hotel.searchAvailableRooms(roomType)) {
            System.out.println(room);
        }

        // Make Reservation
        System.out.print("Enter room number to reserve: ");
        int roomNumber = scanner.nextInt();
        System.out.print("Enter check-in date (YYYY-MM-DD): ");
        LocalDate checkIn = LocalDate.parse(scanner.next());
        System.out.print("Enter check-out date (YYYY-MM-DD): ");
        LocalDate checkOut = LocalDate.parse(scanner.next());

        Room selectedRoom = null;
        for (Room room : hotel.searchAvailableRooms(roomType)) {
            if (room.getRoomNumber() == roomNumber) {
                selectedRoom = room;
                break;
            }
        }

        if (selectedRoom != null) {
            Reservation reservation = hotel.makeReservation(user, selectedRoom, checkIn, checkOut);
            System.out.println("Reservation successful!");
            System.out.println(reservation);

            // Payment
            Payment payment = new Payment();
            if (payment.processPayment(reservation.calculateTotalCost())) {
                System.out.println("Payment successful. Reservation confirmed!");
            } else {
                System.out.println("Payment failed. Reservation not confirmed.");
            }
        } else {
            System.out.println("Room not available for booking.");
        }

        scanner.close();
    }
}

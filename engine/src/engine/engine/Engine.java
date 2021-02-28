package engine.engine;
import engine.classes.boat.BoatBuilder;
import engine.classes.booking.Booking;
import engine.classes.booking.BookingDetails;
import engine.classes.member.Level;
import engine.classes.member.MemberBuilder;
import engine.classes.member.MemberDetails;
import engine.classes.windows.ScheduleWindow;
import engine.classes.windows.ScheduleWindowBuilder;
import engine.classes.windows.ScheduleWindowDetails;
import engine.customExceptions.*;
import engine.generated.*;
import engine.globals.Globals;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;


@XmlRootElement
public class Engine implements BMSEngine {


    @XmlElementWrapper
    @XmlElements({
            @XmlElement(name = "boat"),
            @XmlElement(type = engine.classes.boat.Boat.class)
    })
    List<engine.classes.boat.Boat> boats = new ArrayList<>();


    @XmlElementWrapper
    @XmlElements({
            @XmlElement(name = "member"),
            @XmlElement(type = engine.classes.member.Member.class)
    })
    List<engine.classes.member.Member> members = new ArrayList<>();


    @XmlElementWrapper
    @XmlElements({
            @XmlElement(name = "schedule window"),
            @XmlElement(type = ScheduleWindow.class)
    })
    List<ScheduleWindow> scheduleWindows = new ArrayList<>();

    @XmlElementWrapper
    @XmlElements({
            @XmlElement(name = "booking"),
            @XmlElement(type = Booking.class)
    })
    List<Booking> bookings = new ArrayList<>();

    List<Integer> activeUsersIDs = new ArrayList<>();

    @Override
    public engine.classes.member.Member accessVerification(String email, String pass){
        for (engine.classes.member.Member m : members) {
            if (m.getEmailAddress().equalsIgnoreCase(email) && m.getPassword().equalsIgnoreCase(pass))
                return m;
        }
        return null;
    }



/*    @Override
    public engine.classes.member.Member accessVerification(String email, String pass) throws UserIsAlreadyActiveException{
        for (engine.classes.member.Member m : members) {
            if (m.getEmailAddress().toLowerCase().equals(email.toLowerCase()) && m.getPassword().toLowerCase().equals(pass.toLowerCase())) {
                if (!isUserActive(m.getID())) {
                    addToActiveUsersList(m.getID());
                    return m;
                } else throw new UserIsAlreadyActiveException("User is already logged in to the system.");
            }
        }
        return null;
    }*/

    @Override
    public boolean isScheduleWindowListEmpty() {
        return this.scheduleWindows.isEmpty();
    }

    @Override
    public ScheduleWindow addNewWindow(ScheduleWindowDetails sw) throws InvalidInputException, JAXBException {
        ScheduleWindow scheduleWindow =new ScheduleWindowBuilder().setName(sw.getName()).setStartTime(sw.getStartTime()).setEndTime(sw.getEndTime()).setType(sw.getBoatType()).createScheduleWindow();
        this.scheduleWindows.add(scheduleWindow);
        saveEngineStateToXML();
        return scheduleWindow;
    }

    @Override
    public List<engine.classes.member.Member> getReadOnlyMembersList() {
        return Collections.unmodifiableList(members);
    }

    @Override
    public void addNewBookingRequest(BookingDetails bookingToAdd) throws JAXBException {
        this.bookings.add(new Booking(bookingToAdd.getUserID(),bookingToAdd.getRequestedBoatType(),
                bookingToAdd.getOtherParticipatingRowersIDs(), bookingToAdd.getWindowID(), bookingToAdd.getRequestedPracticeDate()));
        saveEngineStateToXML();
    }

    @Override
    public List<ScheduleWindow> getReadOnlyScheduleWindowList() {
        return Collections.unmodifiableList(scheduleWindows);
    }

    @Override
    public List<Booking> getReadOnlyBookingRequestsList() {
        return Collections.unmodifiableList(bookings);
    }

    @Override
    public List<engine.classes.boat.Boat> getReadOnlyBoatsListList() {
        return Collections.unmodifiableList(boats);
    }

    @Override
    public void updateMemberName(String memberName, String newName) throws InvalidInputException, NotfoundException, JAXBException {
        engine.classes.member.Member userToUpdate = getMemberFromNameString(memberName);

        if (!userToUpdate.getName().equalsIgnoreCase(newName)) {
            userToUpdate.setName(newName);
            saveEngineStateToXML();
        } else throw new InvalidInputException("Name already identical to previous name.");
    }

    @Override
    public void updateMemberEmail(String memberName, String newEmail) throws InvalidInputException, NotfoundException, JAXBException {
        engine.classes.member.Member userToUpdate = getMemberFromNameString(memberName);

        for (engine.classes.member.Member m : members) {
            if (m != userToUpdate && m.getEmailAddress().equals(newEmail))
                throw new InvalidInputException("Email address is already associated to another account.");
        }
        userToUpdate.setEmailAddress(newEmail);
        saveEngineStateToXML();

    }

    @Override
    public void updateMemberPassword(String memberName, String pass) throws InvalidInputException, NotfoundException, JAXBException {
        engine.classes.member.Member userToUpdate = getMemberFromNameString(memberName);

        if (userToUpdate.getPassword().equals(pass))
            throw new InvalidInputException("Password must be different from current one.");
        else {
            userToUpdate.setPassword(pass);
            saveEngineStateToXML();
        }
    }

    @Override
    public void updateMemberPhone(String memberName, String phone) throws InvalidInputException, NotfoundException, JAXBException {
        engine.classes.member.Member userToUpdate = getMemberFromNameString(memberName);

        for (engine.classes.member.Member m : members) {
            if (userToUpdate != m && phone.equals(m.getPhoneNumber()))
                throw new InvalidInputException("Phone number is already associated to another account.");
            else {
                userToUpdate.setPhoneNumber(phone);
                saveEngineStateToXML();
            }
        }
    }


    public engine.classes.member.Member getMemberFromNameString(String memberName) throws NotfoundException {

        for (engine.classes.member.Member member : members) {
            if (member.getName().equalsIgnoreCase(memberName)) {
                return member;
            }
        }
        throw new NotfoundException("Member not found");
    }

    @Override
    public List<Booking> generateAllFutureBookingRequests(int userID) {

        LocalDate now = LocalDate.now();
        List<Booking> futureRequestsOfUser = new ArrayList<>();
        for (Booking b : bookings) {
            if (b.getMemberOrderedID() == userID || isTheUserPartOfTheRequest(b, userID)) {
                if (now.isBefore(b.getRequestedPracticeDate())) { // print only requested dates that are greater than now date.
                    futureRequestsOfUser.add(b);
                }
            }
        }
        return futureRequestsOfUser;
    }

    @Override
    public List<Booking> generateRequestsHistoryOfTheUSer(int userID) {
        LocalDate now = LocalDate.now();
        LocalDate sevenDaysAgo = now.minusDays(7);
        List<Booking> res = new ArrayList<>();

        for (Booking b : bookings) {
            if (b.getMemberOrderedID() == userID)
                if (b.getRequestedPracticeDate().isAfter(sevenDaysAgo) && b.getRequestedPracticeDate().isBefore(now))
                    res.add(b);
        }

        return Collections.unmodifiableList(res);
    }

    public void addAdminUserForProgramAccess() {
        if (!isMemberEmailAddressExistsInList("admin@gmail.com")) {
            members.add(new MemberBuilder()
                    .setAge(20)
                    .setName("admin")
                    .setJoiningDate(LocalDate.parse("12/12/2020", DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                    .setExpirationDate(LocalDate.parse("12/12/2022", DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                    .setEmailAddress("admin@gmail.com")
                    .setHasPrivateBoat(false)
                    .setIsManager(true)
                    .setLevel(Level.ADVANCED)
                    .setPassword("111")
                    .setPhoneNumber("0525440789")
                    .setComments("")
                    .setPrivateBoatSerialNumber("0")
                    .createMember());
        }
    }

    public Booking retrieveBookingPerID(int theID) {
        for (Booking b : bookings) {
            if (b.getBookingID() == theID)
                return b;
        }
        return null;
    }

    public void editPracticeDate(String newVal, Booking theBookingToChange) throws InvalidInputException, JAXBException {
        try {
            theBookingToChange.setRequestedPracticeDate(LocalDate.parse(newVal));
            saveEngineStateToXML();
        } catch (DateTimeParseException e) {
            throw new InvalidInputException("Illegal date entered.");
        }

    }

    public void editBookingWindow(ScheduleWindow theNewWindow, Booking theBookingToChange) throws JAXBException {
        theBookingToChange.setWindowID(theNewWindow.getID());
        saveEngineStateToXML();
    }

    public void removeBookingRequest(Booking theBookingToChange) throws JAXBException {

        bookings.removeIf(b -> b.getBookingID() == theBookingToChange.getBookingID());
        saveEngineStateToXML();
    }


    public boolean isTheUserPartOfTheRequest(Booking b, int userID) {
        for (engine.classes.member.Member m : retrieveMembersAsListPerID(b.getOtherParticipatingRowersID())) {
            if (userID == m.getID())
                return true;
        }
        return false;
    }

    public void editRequestedBoatType(List<engine.classes.boat.BoatType> updatedBoats, Booking theBookingToChange) throws InvalidInputException, JAXBException {
        if (updatedBoats.size() == 0)
            throw new InvalidInputException("Boat list is empty. Please check that the boats entered are with the legal format.");
        theBookingToChange.setBoatType(updatedBoats);
        saveEngineStateToXML();
    }

    public List<Booking> getBookingList() {
        return Collections.unmodifiableList(this.bookings);
    }

    public List<Booking> getPendingBookingRequests() {

        return Collections.unmodifiableList(this.bookings.stream().filter(b -> !b.isApproved()).collect(Collectors.toList()));
    }


    public ScheduleWindow retrieveScheduleWindowPerID(int windowID) {

        for (ScheduleWindow sw : scheduleWindows) {
            if (sw.getID() == windowID)
                return sw;
        }
        return null;
    }

    public engine.classes.member.Member retrieveMemberPerID(int id) {

        for (engine.classes.member.Member m : members) {
            if (m.getID() == id)
                return m;
        }
        return null;
    }

    public List<engine.classes.member.Member> retrieveMembersAsListPerID(List<Integer> theIDs) {
        List<engine.classes.member.Member> res = new ArrayList<>();

        for (Integer id : theIDs) {
            for (engine.classes.member.Member m : members) {
                if (id.equals(m.getID()))
                    res.add(m);
            }
        }
        return res;
    }


    public void setMembersList(List<engine.classes.member.Member> otherMembers) {
        this.members = otherMembers;
    }


    public void setBookingsList(List<Booking> otherBookings) {
        this.bookings = otherBookings;
    }

    public void setBoatsList(List<engine.classes.boat.Boat> otherBoats) {
        this.boats = otherBoats;
    }

    public void setScheduleWindowList(List<ScheduleWindow> otherWindows) {
        this.scheduleWindows = otherWindows;
    }

    public void saveEngineStateToXML() throws JAXBException {
        File file = new File(Globals.engineStatePath);
        Engine engine = new Engine();
        engine.setBoatsList(this.boats);
        engine.setScheduleWindowList(this.scheduleWindows);
        engine.setBookingsList(this.bookings);
        List<engine.classes.member.Member> membersToSaveWithoutAdmin = getMembersWithoutAdmin();
        engine.setMembersList(membersToSaveWithoutAdmin);

        marshalEngine(engine, file);
    }

    private List<engine.classes.member.Member> getMembersWithoutAdmin() {
        List<engine.classes.member.Member> res = new ArrayList<>();
        for (engine.classes.member.Member m : members)
            if (!m.getName().equalsIgnoreCase("admin"))
                res.add(m);
        return res;
    }

    public void marshalEngine(BMSEngine engine, File file) throws JAXBException {
            JAXBContext jaxbContext = JAXBContext.newInstance(Engine.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(engine, file);

    }

    @Override
    public void addBoat(String boatKind, String boatName, boolean isPrivate) throws InvalidBoatNameException, InvalidTypeException, JAXBException {
        BoatBuilder builder = new BoatBuilder();
        builder.setBoatName(boatName);
        builder.setPrivate(isPrivate);

        String[] boatKindArr = boatKind.split(" ");
        engine.classes.boat.BoatType boatType = getBoatTypeFromString(boatKindArr[0]);

        if (boatKindArr.length >= 2) {
            if (boatKindArr[1].equalsIgnoreCase("wide"))
                builder.setWide(true);
            else if (boatKindArr[1].equalsIgnoreCase("coastal"))
                builder.setCoastal(true);
        }
        builder.setBoatType(boatType);

        boats.add(builder.getResult());
        saveEngineStateToXML();
    }

    //return null if didn't found
    public engine.classes.boat.BoatType getBoatTypeFromString(String boatStringRepresentation) {
        engine.classes.boat.BoatType boatType = null;
        for (engine.classes.boat.BoatType CurrBoatType : engine.classes.boat.BoatType.values()) {
            if (CurrBoatType.getSymbol().equalsIgnoreCase(boatStringRepresentation)) {
                boatType = CurrBoatType;
                break;
            }
        }
        return boatType;
    }

    @Override
    public void addMember(MemberDetails member) throws JAXBException {
        members.add(new MemberBuilder().
                setEmailAddress(member.getEmailAddress())
                .setComments(member.getComments())
                .setName(member.getName())
                .setPassword(member.getPassword())
                .setPhoneNumber(member.getPhoneNumber())
                .setAge(member.getAge())
                .setExpirationDate(member.getExpirationDate())
                .setHasPrivateBoat(member.isHasPrivateBoat())
                .setIsManager(member.isManager())
                .setJoiningDate(member.getJoiningDate())
                .setLevel(member.getLevel())
                .setPrivateBoatSerialNumber(member.getPrivateBoatSerialNumber())
                .createMember());
        saveEngineStateToXML();
    }

    @Override
    public void addBookingRequest(Booking booking) {

    }

    @Override
    public void addActivity(ScheduleWindow activity) {
    }

    @Override
    public void removeBoat(String boatId, boolean forceRemove) throws NotfoundException, NeedToChangeOtherListingException, JAXBException {
        StringBuilder toBeRemovedNotification = new StringBuilder();
        boolean found = false;
        engine.classes.boat.Boat boatToRemove = null;
        for (engine.classes.boat.Boat boat : boats) {
            if (boat.getID() == Integer.parseInt(boatId)) {
                found = true;
                boatToRemove = boat;
                break;
            }
        }

        if (!found) throw new NotfoundException("Boat not found.");

        List<Booking> bookingsToRemove = new ArrayList<>();
        List<engine.classes.member.Member> membersToRemovePrivateBoat = new ArrayList<>();

        for (Booking booking : bookings) {
            if (booking.getAssignedBoatID() == Integer.parseInt(boatId)) {
                bookingsToRemove.add(booking);
            }
        }
        if (!bookingsToRemove.isEmpty() && !forceRemove) {
            toBeRemovedNotification.append("Need to remove bookings: ").append(bookingsToRemove.stream().map(Booking::getBookingID).collect(Collectors.toList())).append(System.lineSeparator());
        }

        for (engine.classes.member.Member member : members) {
            if (member.hasPrivateBoat() &&
                    member.getPrivateBoatSerialNumber().equalsIgnoreCase(String.valueOf(boatToRemove.getID()))) {
                toBeRemovedNotification.append("Need to remove: ").append(member.getName()).append(System.lineSeparator());
                membersToRemovePrivateBoat.add(member);
            }
        }

        if(toBeRemovedNotification.length() == 0)
            boats.remove(boatToRemove);

        else if (!forceRemove) throw new NeedToChangeOtherListingException(toBeRemovedNotification.toString());

        for (Booking booking : bookingsToRemove)
            removeBookingRequest(booking);

        for (engine.classes.member.Member member : membersToRemovePrivateBoat) {
            member.removePrivateBoat();
        }

        boats.remove(boatToRemove);
        saveEngineStateToXML();
    }

    @Override
    public void removeMember(String memberId) throws NotfoundException, JAXBException {
        boolean found = false;
        engine.classes.member.Member memberToRemove = null;
        for (engine.classes.member.Member member : members) {
            if (member.getID() == Integer.parseInt(memberId)) {
                found = true;
                memberToRemove = member;
                break;
            }
        }

        if (!found) throw new NotfoundException("Member not found.");
        else {
            List<Booking> bookingsToRemove = new ArrayList<>();

            for (Booking booking : bookings) {
                if (booking.getMemberOrderedID() == Integer.parseInt(memberId)) {
                    bookingsToRemove.add(booking);
                }
            }

            for (Booking booking : bookingsToRemove)
                removeBookingRequest(booking);

            if (memberToRemove.hasPrivateBoat()) {
                try {
                    removeBoat(memberToRemove.getPrivateBoatSerialNumber(), true);
                } catch (NeedToChangeOtherListingException e) {}
            }

            for (Booking booking : bookings) {
                if (booking.isInParticipatingRowers(memberToRemove.getID())) {
                    booking.removeParticipatingRower(memberToRemove.getID());
                }
            }

            members.remove(memberToRemove);

        }
        saveEngineStateToXML();
    }

    @Override
    public void removeBookingRequest(String bookingRequestId) throws NotfoundException {
        boolean found = false;
        Booking bookingToRemove = null;
        for (Booking booking : bookings) {
            if (booking.getBookingID() == Integer.parseInt(bookingRequestId)) {
                found = true;
                bookingToRemove = booking;
                break;
            }
        }

        if (!found) throw new NotfoundException("Booking not found.");
        else bookings.remove(bookingToRemove);

    }

    @Override
    public void removeScheduleWindow(String scheduleWindowName) throws NotfoundException {
        boolean found = false;
        ScheduleWindow scheduleWindowToRemove = null;
        for (ScheduleWindow scheduleWindow : scheduleWindows) {
            if (boats.isEmpty()) break;
            if (scheduleWindow.getName().equals(scheduleWindowName)) {
                found = true;
                scheduleWindowToRemove = scheduleWindow;
                break;
            }
        }

        if (!found) throw new NotfoundException("Schedule window not found.");
        else scheduleWindows.remove(scheduleWindowToRemove);
    }

    @Override
    public void updateScheduleWindowName(String scheduleWindowName, String newName) throws InvalidInputException, NotfoundException {
        ScheduleWindow windowToUpdate = getScheduleWindowFromNameString(scheduleWindowName);

        if (!windowToUpdate.getName().equals(newName))
            windowToUpdate.setName(newName);
        else throw new InvalidInputException("Name already identical to previous name.");
    }


    @Override
    public void updateScheduleWindowStartTime(String scheduleWindowName, LocalTime time) throws InvalidInputException, NotfoundException {
        ScheduleWindow windowToUpdate = getScheduleWindowFromNameString(scheduleWindowName);

        if (windowToUpdate.getEndTime().isAfter(time))
            windowToUpdate.setStartTime(time);
        else
            throw new InvalidInputException("Start time must be before end time. (current endTime:" + windowToUpdate.getEndTime() + ")");
    }

    @Override
    public void updateScheduleWindowEndTime(String scheduleWindowName, LocalTime time) throws InvalidInputException, NotfoundException {
        ScheduleWindow windowToUpdate = getScheduleWindowFromNameString(scheduleWindowName);

        if (windowToUpdate.getStartTime().isBefore(time))
            windowToUpdate.setEndTime(time);
        else
            throw new InvalidInputException("Start time must be before end time. (current endTime:" + windowToUpdate.getEndTime() + ")");
    }

    @Override
    public void updateScheduleWindowBoatType(String scheduleWindowName, engine.classes.boat.BoatType boatType) throws InvalidInputException, NotfoundException {
        ScheduleWindow windowToUpdate = getScheduleWindowFromNameString(scheduleWindowName);

        if (!windowToUpdate.getBoatType().equals(boatType))
            windowToUpdate.setBoatType(boatType);
        else throw new InvalidInputException("Boat Type already identical to previous type.");
    }

    @Override
    public engine.classes.boat.Boat retrieveBoatPerID(int assignedBoatID) {
        for (engine.classes.boat.Boat b : boats) {
            if (b.getID() == assignedBoatID)
                return b;
        }
        return null;
    }

    private ScheduleWindow getScheduleWindowFromNameString(String scheduleWindowName) throws NotfoundException {
        ScheduleWindow windowToUpdate = null;
        boolean found = false;

        for (ScheduleWindow scheduleWindow : scheduleWindows) {
            if (scheduleWindow.getName().equalsIgnoreCase(scheduleWindowName)) {
                windowToUpdate = scheduleWindow;
                found = true;
                break;
            }

        }
        if (!found)
            throw new NotfoundException("Schedule window not found.");
        return windowToUpdate;
    }

    @Override
    public void loadEngine(File file) throws JAXBException {
        Engine engine = unmarshalEngine(file);
        setAllClassesMaxIDs(engine);
        setMaxIDInMembers(engine.members);
        assignLists(engine);

    }

    private void setAllClassesMaxIDs(Engine engine) {
        setMaxIDInBoats(engine.boats);
        setMaxIDInMembers(engine.members);
        setMaxIDInBookings(engine.bookings);
        setMaxIDInWindows(engine.scheduleWindows);
    }

    private Engine unmarshalEngine(File filePath) throws JAXBException {
        Engine res;
        JAXBContext jaxbContext = JAXBContext.newInstance(Engine.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        res = (Engine) unmarshaller.unmarshal(filePath);
        return res;
    }

    @Override
    public void assignLists(BMSEngine engine) {
        if (!engine.getMembers().isEmpty())
            this.setMembersList(engine.getMembers());
        if (!engine.getBoats().isEmpty())
            this.setBoatsList(engine.getBoats());
        if (!engine.getBookings().isEmpty())
            this.setBookingsList(engine.getBookings());
        if (!engine.getScheduleWindows().isEmpty())
            this.setScheduleWindowList(engine.getScheduleWindows());

    }

    @Override
    public void updateMemberAge(String memberName, int ageFromUser) throws NotfoundException, JAXBException {
        engine.classes.member.Member userToUpdate = getMemberFromNameString(memberName);

        userToUpdate.setAge(ageFromUser);
        saveEngineStateToXML();
    }

    @Override
    public void updateMemberComments(String memberName, String commentsFromUser) throws NotfoundException, JAXBException {
        engine.classes.member.Member userToUpdate = getMemberFromNameString(memberName);

        userToUpdate.replaceComments(commentsFromUser);
        saveEngineStateToXML();

    }

    @Override
    public void updateMemberExpirationDate(String memberName, LocalDate expirationDateFromUser) throws InvalidInputException, NotfoundException, JAXBException {
        engine.classes.member.Member userToUpdate = getMemberFromNameString(memberName);

        if (userToUpdate.getJoiningDate().isAfter(expirationDateFromUser))
            throw new InvalidInputException("expiration date cant be before joining date.\n(joining date is: " +
                    userToUpdate.getJoiningDate() + ")");
        else {
            userToUpdate.setExpirationDate(expirationDateFromUser);
            saveEngineStateToXML();
        }
    }

    @Override
    public void updateMemberHasPrivateBoat(String memberName, boolean fromUserIfHasPrivateBoat) throws NotfoundException, JAXBException {
        engine.classes.member.Member userToUpdate = getMemberFromNameString(memberName);


        userToUpdate.setHasPrivateBoat(fromUserIfHasPrivateBoat);
        saveEngineStateToXML();

    }

    @Override
    public void updateMemberPrivateBoatSerialNumber(String memberName, String privateBoatSerialNumber) throws NotfoundException, JAXBException {
        if (privateBoatSerialNumber.isEmpty())
            updateMemberHasPrivateBoat(memberName, false);
        else {

            engine.classes.member.Member userToUpdate = getMemberFromNameString(memberName);

            userToUpdate.setPrivateBoatSerialNumber(privateBoatSerialNumber);

            saveEngineStateToXML();

        }


    }

    @Override
    public void updateMemberIsManager(String memberName, boolean isManager) throws NotfoundException, JAXBException {
        engine.classes.member.Member userToUpdate = getMemberFromNameString(memberName);
        userToUpdate.setAdmin(isManager);
        saveEngineStateToXML();
    }

    @Override
    public void updateBoatName(String boatIDToUpdate, String nameFromUser) throws InvalidInputException, NotfoundException, JAXBException {
        engine.classes.boat.Boat boatToUpdate = getBoatFromID(Integer.parseInt(boatIDToUpdate));

        if (!boatToUpdate.getBoatName().equals(nameFromUser)) {
            boatToUpdate.setName(nameFromUser);
            saveEngineStateToXML();
        } else throw new InvalidInputException("Name already identical to previous name.");
    }


    @Override
    public void updateBoatType(String boatIDToUpdate, engine.classes.boat.BoatType boatTypeFromUser) throws InvalidInputException, NotfoundException, JAXBException {
        engine.classes.boat.Boat boatToUpdate = getBoatFromID(Integer.parseInt(boatIDToUpdate));

        if (!boatToUpdate.getBoatType().equals(boatTypeFromUser)) {
            boatToUpdate.setBoatType(boatTypeFromUser);
            saveEngineStateToXML();
        } else throw new InvalidInputException("BoatType already identical to previous type.");
    }

    @Override
    public void updateBoatIsPrivate(String boatIDToUpdate, boolean isPrivate) throws NotfoundException, JAXBException {
        engine.classes.boat.Boat boatToUpdate = getBoatFromID(Integer.parseInt(boatIDToUpdate));

        if (!isPrivate && boatToUpdate.isPrivateBoat()) {
            for (engine.classes.member.Member member : members) {
                if (member.getPrivateBoatSerialNumber().equalsIgnoreCase(String.valueOf(boatToUpdate.getID()))) {
                    updateMemberHasPrivateBoat(member.getName(), false);
                    break;
                }
            }
        }

        boatToUpdate.setIsPrivate(isPrivate);

        saveEngineStateToXML();
    }

    @Override
    public void updateBoatIsWide(String boatIDToUpdate, boolean isWide) throws InvalidInputException, NotfoundException, JAXBException {
        engine.classes.boat.Boat boatToUpdate = getBoatFromID(Integer.parseInt(boatIDToUpdate));

        if (!(boatToUpdate.isWideBoat() == isWide)) {
            boatToUpdate.setIsWide(isWide);
            saveEngineStateToXML();
        } else throw new InvalidInputException("Boat " + (isWide ? "is" : "isn't") + "wide already?");
    }

    @Override
    public void updateBoatHasCoxswain(String boatIDToUpdate, boolean hasCoxswain) throws InvalidInputException, NotfoundException, JAXBException {
        engine.classes.boat.Boat boatToUpdate = getBoatFromID(Integer.parseInt(boatIDToUpdate));

        if (!(boatToUpdate.hasCoxswain() == hasCoxswain)) {
            boatToUpdate.setHasCoxswain(hasCoxswain);
            saveEngineStateToXML();
        } else throw new InvalidInputException("Boat " + (hasCoxswain ? "have" : "doesn't have") + "coxswain already?");
    }

    @Override
    public void updateBoatIsCoastal(String boatIDToUpdate, boolean isCoastal) throws InvalidInputException, NotfoundException, JAXBException {
        engine.classes.boat.Boat boatToUpdate = getBoatFromID(Integer.parseInt(boatIDToUpdate));

        if (!(boatToUpdate.isCoastal() == isCoastal)) {
            boatToUpdate.setIsCoastal(isCoastal);
            saveEngineStateToXML();
        } else throw new InvalidInputException("Boat " + (isCoastal ? "is" : "isn't") + "coastal already?");
    }

    @Override
    public void updateBoatIsFunctioning(String boatIDToUpdate, boolean isFunctioning) throws InvalidInputException, NotfoundException, JAXBException {
        engine.classes.boat.Boat boatToUpdate = getBoatFromID(Integer.parseInt(boatIDToUpdate));

        if (!(boatToUpdate.isFunctioningBoat() == isFunctioning)) {
            boatToUpdate.setIsFunctioning(isFunctioning);
            saveEngineStateToXML();
        } else throw new InvalidInputException("Boat " + (isFunctioning ? "is" : "isn't") + "functioning already?");
    }

    private engine.classes.boat.Boat getBoatFromID(int boatIDToFind) throws NotfoundException {
        engine.classes.boat.Boat boatToUpdate = null;
        boolean found = false;


        for (engine.classes.boat.Boat boat : boats) {
            if (boat.getID() == boatIDToFind) {
                boatToUpdate = boat;
                found = true;
                break;
            }
        }
        if (!found)
            throw new NotfoundException("Boat ID not found.");
        return boatToUpdate;
    }

    @Override
    public void updateBookingPracticeDate(String bookingIDToUpdate, LocalDate practiceDate) throws InvalidInputException, JAXBException {
        Booking bookingToUpdate = retrieveBookingPerID(Integer.parseInt(bookingIDToUpdate));

        if (bookingToUpdate.getRequestedPracticeDate() == practiceDate)
            throw new InvalidInputException("this practice date is already set");
        else {
            bookingToUpdate.setRequestedPracticeDate(practiceDate);
            saveEngineStateToXML();
        }
    }


    @Override
    public void updateBookingRequestedWindowID(String bookingIDToUpdate, String requestedWindowID) throws InvalidInputException, JAXBException {
        Booking bookingToUpdate = retrieveBookingPerID(Integer.parseInt(bookingIDToUpdate));

        if (!isWindowValid(retrieveScheduleWindowPerID(Integer.parseInt(requestedWindowID))))
            throw new InvalidInputException("this window is not valid");
        else {
            bookingToUpdate.setWindowID(Integer.parseInt(requestedWindowID));
            saveEngineStateToXML();
        }
    }

    private boolean isWindowValid(ScheduleWindow windowToCheck) {
        if (scheduleWindows.isEmpty()) return true;
        for (ScheduleWindow currWindow : scheduleWindows) {
            if (currWindow.getStartTime() == windowToCheck.getStartTime() &&
                    currWindow.getEndTime() == windowToCheck.getEndTime()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void updateBookingAssignedBoatID(int bookingIDToUpdate, int assignedBoatID) throws BoatAssignmentException, JAXBException {
        Booking bookingToUpdate = retrieveBookingPerID(bookingIDToUpdate);

        validateBoatAssignment(retrieveBoatPerID(assignedBoatID), bookingToUpdate); // throws an exception if the boat isn't valid

        bookingToUpdate.setAssignedBoatID(assignedBoatID);
        saveEngineStateToXML();
    }

    private void validateBoatAssignment(engine.classes.boat.Boat boat, Booking bookingToUpdate) throws BoatAssignmentException {
        int numRowersInBooking = bookingToUpdate.getOtherParticipatingRowersID().size() + 1;
        int boatCapacity = boat.getBoatType().getRowersAmount();

        if (numRowersInBooking > boatCapacity) {
            throw new BoatAssignmentException("Number of rowers is bigger than the boat capacity. Please assign bigger boat or remove some of the rowers from this order" +
                    "\nBoat capacity: " + boat.getBoatType().getRowersAmount() + "\nNumber of rowers in booking: " + bookingToUpdate.getNumberParticipates());
        } else if (numRowersInBooking < boatCapacity)
            throw new BoatAssignmentException("Number of rowers is less than the boat capacity. Please assign smaller boat or add more rowers to this order." +
                    "\nBoat Capacity: " + boat.getBoatType().getRowersAmount() + "\nNumber of rowers in booking: " + bookingToUpdate.getNumberParticipates());
    }

    @Override
    public void updateBookingRequestedBoatTypes(String bookingIDToUpdate, List<engine.classes.boat.BoatType> boatTypesFromUser) throws InvalidInputException, JAXBException {
        Booking bookingToUpdate = retrieveBookingPerID(Integer.parseInt(bookingIDToUpdate));

        if (!isBoatTypesValidForBooking(boatTypesFromUser, bookingToUpdate)) {
            throw new InvalidInputException("this boat types is not valid for the booking");
        } else {
            bookingToUpdate.setBoatType(boatTypesFromUser);
            saveEngineStateToXML();
        }
    }

    private boolean isBoatTypesValidForBooking(List<engine.classes.boat.BoatType> boatTypesFromUser, Booking bookingToUpdate) {
        for (engine.classes.boat.BoatType boatType : boatTypesFromUser){
            if (boatType.getRowersAmount() < bookingToUpdate.getOtherParticipatingRowersID().size() + 1){
                return false;
            }
        }
        return true;
    }

    @Override
    public void updateBookingOtherParticipatingRowersID(String bookingIDToUpdate, List<Integer> participatingRowersIDs) throws InvalidInputException, JAXBException {
        Booking bookingToUpdate = retrieveBookingPerID(Integer.parseInt(bookingIDToUpdate));

        if (!isOtherParticipatingRowersIDValidForBooking(participatingRowersIDs)) {
            throw new InvalidInputException("this Other Participating Rowers is not valid for the booking");
        } else {
            bookingToUpdate.setParticipatingRowers(participatingRowersIDs);
            saveEngineStateToXML();
        }
    }

    private boolean isOtherParticipatingRowersIDValidForBooking(List<Integer> participatingRowersIDs) {
        for (Integer id : participatingRowersIDs){
            if (retrieveMemberPerID(id) == null)
                return false;
        }
        return true;
    }

    public void importBoatsFromXML(boolean runOverCurrentList, String fileAsString) throws ImportXmlException, InvalidBoatNameException, InvalidTypeException {
        Boats boats;
        try {
            boats = deserializeBoatsFrom(fileAsString);
            List<String> corruptedRecords = new ArrayList<>();

            List<engine.generated.Boat> validBoats = generateValidImportedBoatsList(boats.getBoat(),corruptedRecords);
            if (runOverCurrentList)
                allocateBoatsAndReplaceWithCurrentList(validBoats);
            else addBoatsToCurrentList(validBoats);
            saveEngineStateToXML();
            if (corruptedRecords.size() > 0)
                throw new ImportXmlException("The following records IDs were not imported due to data corruption: " + corruptedRecords.toString());

        } catch (JAXBException | NotfoundException e) {
            throw new ImportXmlException(e.getMessage());
        }

    }

    public void importMembersFromXML(boolean runOverCurrentList, String fileAsString) throws ImportXmlException {
        Members members;

        try {
            members = deserializeMembersFrom(fileAsString);
            List<String> corruptedRecords = new ArrayList<>();
            List<engine.generated.Member> validMembers = generatedValidImportedMembersList(members,corruptedRecords);
            if (runOverCurrentList)
                allocateMembersAndReplaceWithCurrentList(validMembers);
            else addMembersToCurrentList(validMembers);
            saveEngineStateToXML();
            if (corruptedRecords.size() > 0)
                throw new ImportXmlException("The following records' IDs were not imported from file due to data corruption: " + corruptedRecords.toString());
        } catch (JAXBException e) {
            throw new ImportXmlException(e.getMessage());
        }
    }
    public void importWindowsFromXML(boolean runOverCurrentList, String fileAsString) throws ImportXmlException, InvalidInputException {
        Activities activities;
        List<String> corruptedRecords = new ArrayList<>();
        List<Timeframe> validRecords;
        try {
            activities = deserializeActivitiesFrom(fileAsString);
            validRecords = generateValidImportedActivitiesList(activities, corruptedRecords);
            if (runOverCurrentList)
                allocateActivitiesAndReplaceWithCurrentList(validRecords);
            else addActivitiesToCurrentList(validRecords);
            saveEngineStateToXML();
            if (corruptedRecords.size() > 0)
                throw new ImportXmlException("The following records couldn't get imported to system due to data corruption: " + corruptedRecords.toString());
        } catch (JAXBException e) {
            throw new ImportXmlException(e.getMessage());
        }
    }


    public Boats deserializeBoatsFrom(String in) throws JAXBException {

        JAXBContext jaxbContext = JAXBContext.newInstance(Boats.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        return (Boats) unmarshaller.unmarshal(new StringReader(in));
    }

    public Members deserializeMembersFrom(String in) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Members.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        return (Members) unmarshaller.unmarshal(new StringReader(in));
    }

    public Activities deserializeActivitiesFrom(String in) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Activities.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        return (Activities) unmarshaller.unmarshal(new StringReader(in));
    }


    public List<engine.generated.Boat> generateValidImportedBoatsList(List<engine.generated.Boat> boatsParam, List<String> corruptedRecords) {

        Set<Integer> set = new HashSet<>();
        List<engine.generated.Boat> res = new ArrayList<>();

        if (boatsParam.isEmpty())
            return null;

        for (Boat b : boatsParam){
            if (set.add(Integer.parseInt(b.getId())) && !b.getName().equals("") && !b.getId().equals("") && Integer.parseInt(b.getId()) > 0)
                res.add(b);
            else corruptedRecords.add(b.getId());
        }
        return res;
    }

    public List<engine.generated.Member> generatedValidImportedMembersList(Members members, List<String> corruptedRecords){
        Set<Integer> set = new HashSet<>();
        List<engine.generated.Member> res = new ArrayList<>();
        if (members.getMember().isEmpty())
            return null;

            for (Member m : members.getMember()) {
                if (set.add(Integer.parseInt(m.getId()))){
                    if (Integer.parseInt(m.getId()) > 0 && m.getAge() > 0){
                        if (m.isHasPrivateBoat() != null){
                            if (m.isHasPrivateBoat()){
                                if (m.getPrivateBoatId().length() > 0) {
                                    res.add(m);
                                }
                                else corruptedRecords.add(m.getId());
                            }
                            else res.add(m);
                        }
                        else res.add(m);
                    }
                    else corruptedRecords.add(m.getId());
                }
                else corruptedRecords.add(m.getId());
            }
        return res;
    }

    public List<Timeframe> generateValidImportedActivitiesList(Activities activities, List<String> corruptedRecords){
        String timeRegex = "^([0-1][0-9]|[2][0-3]):([0-5][0-9])$";
        List<Timeframe> res = new ArrayList<>();
        if (activities.getTimeframe().isEmpty())
            return null;
        for (Timeframe t : activities.getTimeframe()) {
            if (t.getName().length() > 0 && t.getStartTime().matches(timeRegex) && t.getEndTime().matches(timeRegex))
                res.add(t);
            else corruptedRecords.add(t.getName());
        }
        return res;
    }

    public void allocateBoatsAndReplaceWithCurrentList(List<engine.generated.Boat> generatedBoats) throws InvalidTypeException, InvalidBoatNameException {

        this.boats.clear();
        for (engine.generated.Boat b : generatedBoats) {
            this.boats.add(createBoat(b));
        }
        new engine.classes.boat.Boat().setCounter(getMaxID(generateBoatsIDsList(this.boats)));
    }

    public void allocateMembersAndReplaceWithCurrentList(List<Member> members) {
        for (Member m :members){
            if (isMemberEmailAddressExistsInList(m.getEmail())){
                engine.classes.member.Member existingMemberRecord = retrieveMemberByEmailAddress(m.getEmail());
                if (existingMemberRecord != null){
                        int idToFree = existingMemberRecord.getID();
                        int newIDVal = Integer.parseInt(m.getId());
                        replaceBetweenMemberIDAndParamIDInSystem(idToFree,newIDVal);
                }
            }
        }
        this.members.clear();
        for (Member m : members)
            this.members.add(createMember(m));
    }

    public void allocateActivitiesAndReplaceWithCurrentList(List<Timeframe> validRecords) throws InvalidInputException {
        this.scheduleWindows.clear();
        for (Timeframe t : validRecords)
            this.scheduleWindows.add(createScheduleWindow(t));
    }

    public void addBoatsToCurrentList(List<engine.generated.Boat> generatedBoats) throws NotfoundException, InvalidTypeException, InvalidBoatNameException {
        for (engine.generated.Boat boat : generatedBoats) {
            if (!isBoatNameExists(boat.getName())) {
                if (isBoatIDExists(Integer.parseInt(boat.getId()))) {
                    replaceBetweenBoatIDAndParamIDInSystem(Integer.parseInt(boat.getId()),
                            getMaxID(generateBoatsIDsList(this.boats)) + 1);
                }
                this.boats.add(createBoat(boat));
            }
        }
        new engine.classes.boat.Boat().setCounter(getMaxID(generateBoatsIDsList(this.boats)));
    }


    private void replaceBetweenBoatIDAndParamIDInSystem(int idToFree, int newIDVal) throws NotfoundException {

        if (getBoatFromID(idToFree).isPrivateBoat()){
            checkForMemberOwnershipAndUpdate(idToFree,newIDVal);
        searchAndUpdateBoatIDInApprovedBookings(idToFree,newIDVal);
        engine.classes.boat.Boat boatToUpdate = getBoatFromID(idToFree);
        updateGivenEngineObjectID(boatToUpdate,newIDVal,boatToUpdate.getClass().getName());
    }


}

    private void updateGivenEngineObjectID(Object objectToUpdate, int newIDVal, String className) {
        switch (className){
            case "Boat":
                engine.classes.boat.Boat b =(engine.classes.boat.Boat) objectToUpdate;
                b.setBoatID(newIDVal);
                break;
            case "Member":
                engine.classes.member.Member m = (engine.classes.member.Member) objectToUpdate;
                m.setID(newIDVal);
                break;

        }
    }

    private void searchAndUpdateBoatIDInApprovedBookings(int idToFree, int newIDVal) {
        for (Booking b: this.bookings){
            if (b.isApproved())
                if (b.getAssignedBoatID() == idToFree)
                    b.setAssignedBoatID(newIDVal);
        }
    }

    private void checkForMemberOwnershipAndUpdate(int idToFree, int newIDVal) {

        for (engine.classes.member.Member m: this.members){
            if (m.hasPrivateBoat())
                if (Integer.parseInt(m.getPrivateBoatSerialNumber()) == idToFree)
                    m.setPrivateBoatSerialNumber(String.valueOf(newIDVal));
        }

    }

    public void addMembersToCurrentList(List<Member> members) {
        for (Member m : members) {
            if (!isMemberEmailAddressExistsInList(m.getEmail())){
                if (isMemberIDExists(Integer.parseInt(m.getId()))){
                    replaceBetweenMemberIDAndParamIDInSystem(Integer.parseInt(m.getId()),
                            getMaxID(generateMembersIDsList(this.members)) + 1);
                }
                this.members.add(createMember(m));
            }
        }
        new engine.classes.member.Member().setCounter(getMaxID(generateMembersIDsList(this.members)));
    }

    private void replaceBetweenMemberIDAndParamIDInSystem(int idToFree, int newIDVal) {
        List<Booking> associatedBookingsToMember = retrieveBookingsAssociatedToMemberID(idToFree);

        for (Booking b: associatedBookingsToMember){
            if (b.getMemberOrderedID() == idToFree)
                b.setMemberOrderedID(newIDVal);
            else replaceIDInOtherParticipatingRowersList(b.getOtherParticipatingRowersID(),idToFree,newIDVal);
        }

        if (this.activeUsersIDs.contains(idToFree)) {
            this.activeUsersIDs.remove((Integer) idToFree);
            this.activeUsersIDs.add(newIDVal);
        }
        engine.classes.member.Member memberToChangeID = retrieveMemberPerID(idToFree);
        memberToChangeID.setID(newIDVal);
    }

    private void replaceIDInOtherParticipatingRowersList(List<Integer> otherParticipatingRowersID, int idToFree, int newIDVal) {
        if (otherParticipatingRowersID.contains(idToFree))
            otherParticipatingRowersID.remove((Integer)idToFree);
        otherParticipatingRowersID.add(newIDVal);
    }

    private List<Booking> retrieveBookingsAssociatedToMemberID(int idToFree) {
        List<Booking> res = new ArrayList<>();
        for (Booking b :bookings){
            if (isTheUserPartOfTheRequest(b,idToFree) || b.getMemberOrderedID() == idToFree)
                res.add(b);
        }
        return res;
    }

    @Override
    public boolean isMemberIDExists(int id) {
        for (engine.classes.member.Member m :members)
            if (m.getID() == id)
                return true;
        return false;
    }


    private engine.classes.member.Member retrieveMemberByEmailAddress(String email) {
        for (engine.classes.member.Member m : members)
            if (m.getEmailAddress().equalsIgnoreCase(email))
                return m;
        return null;
    }


    public void addActivitiesToCurrentList(List<Timeframe> validRecords) throws InvalidInputException {
        for (Timeframe t : validRecords) {
            if (!isActivityNameExistsInList(t.getName()))
                this.scheduleWindows.add(createScheduleWindow(t));
        }
    }
    public boolean isBoatIDExists(int boatID){
        for (Integer id: generateBoatsIDsList(this.boats))
            if (id == boatID)
                return true;
        return false;
    }
    public boolean isBoatNameExists(String boatName) {
        for (engine.classes.boat.Boat b : this.boats) {
            if (b.getBoatName().equalsIgnoreCase(boatName))
                return true;
        }
        return false;
    }

    public boolean isMemberEmailAddressExistsInList(String email) {
        for (engine.classes.member.Member m : this.members) {
            if (m.getEmailAddress().equalsIgnoreCase(email))
                return true;
        }
        return false;
    }

    public boolean isActivityNameExistsInList(String name) {
        for (ScheduleWindow s : this.scheduleWindows) {
            if (s.getName().equalsIgnoreCase(name))
                return true;
        }
        return false;
    }

    public engine.classes.boat.Boat createBoat(Boat b) throws InvalidBoatNameException, InvalidTypeException {
        BoatBuilder builder = new BoatBuilder();
        engine.classes.boat.Boat res;
        builder.setBoatName(b.getName());
        builder.setBoatType(engine.classes.boat.BoatType.valueOf(b.getType().toString()));

        builder.setBoatID(Integer.parseInt(b.getId()));
        if (b.isCostal() == null)
            builder.setCoastal(false);
        else builder.setCoastal(b.isCostal());
        if (b.isOutOfOrder() == null)
            builder.setFunctioning(false);
        else builder.setFunctioning(b.isOutOfOrder());
        if (b.isHasCoxswain() == null)
            builder.setHasCoxswain(false);
        else builder.setHasCoxswain(b.isHasCoxswain());
        if (b.isPrivate() == null)
            builder.setPrivate(false);
        else builder.setPrivate(b.isPrivate());
        if (b.isWide() == null)
            builder.setWide(false);
        else builder.setWide(b.isWide());
            res = builder.getResult();

        return res;
    }

    public engine.classes.member.Member createMember(Member m) {
        MemberBuilder builder = new MemberBuilder();
        builder.setName(m.getName());
        builder.setPassword(m.getPassword());
        builder.setEmailAddress(m.getEmail());

        builder.setID(Integer.parseInt(m.getId()));
        if (m.isManager() == null)
            builder.setIsManager(false);
        else builder.setIsManager(m.isManager());
        if (m.isHasPrivateBoat() == null)
            builder.setHasPrivateBoat(false);
        else {
            builder.setHasPrivateBoat(m.isHasPrivateBoat());
            if (m.getPrivateBoatId() == null)
                builder.setPrivateBoatSerialNumber("");
            else builder.setPrivateBoatSerialNumber(m.getPrivateBoatId());
        }
        if (m.getComments() == null || m.getComments().isEmpty())
            builder.setComments("");
        else builder.setComments(m.getComments());
        if (m.getLevel() == null)
            builder.setLevel(null);
        else builder.setLevel(Level.valueOf(m.getLevel().toString()));
        if (m.getJoined() == null)
            builder.setJoiningDate(null);
        else builder.setJoiningDate(m.getJoined().toGregorianCalendar().toZonedDateTime().toLocalDate());
        if (m.getMembershipExpiration() == null)
            builder.setExpirationDate(null);
        else
            builder.setExpirationDate(m.getMembershipExpiration().toGregorianCalendar().toZonedDateTime().toLocalDate());
        builder.setAge(m.getAge());

        return builder.createMember();

    }


    public ScheduleWindow createScheduleWindow(Timeframe t) throws InvalidInputException {
        ScheduleWindowBuilder builder = new ScheduleWindowBuilder();
        builder.setName(t.getName());
        builder.setStartTime(LocalTime.parse(t.getStartTime()));
        builder.setEndTime(LocalTime.parse(t.getEndTime()));
        if (t.getBoatType() == null)
            builder.setType(null);
        else builder.setType(engine.classes.boat.BoatType.valueOf(t.getBoatType().toString()));

        return builder.createScheduleWindow();
    }

    public boolean isPrivateBoatIDExistsInBoatsList(int idToFind) {
        for (engine.classes.boat.Boat b : boats) {
            if (b.getID() == idToFind)
                return true;
        }
        return false;
    }

    public String exportMembersToXml() throws DatatypeConfigurationException,ExportToXmlException{
        Members members = new Members();

        for (engine.classes.member.Member m: this.members){
            Member member = new Member();
            member.setAge(m.getAge());
            member.setComments(m.getComments());
            member.setEmail(m.getEmailAddress());
            member.setHasPrivateBoat(m.hasPrivateBoat());
            member.setId(Integer.toString(m.getID()));
            member.setName(m.getName());

            if (m.getLevel() != null)
                member.setLevel(RowingLevel.valueOf(m.getLevel().toString()));
            if (m.getJoiningDate() != null){
                XMLGregorianCalendar join = convertToGregorianCalendar(LocalDate.parse(m.getJoiningDate().toString()));
                member.setJoined(join);
            }
            if (m.getExpirationDate() != null){
                XMLGregorianCalendar exp = convertToGregorianCalendar(LocalDate.parse(m.getExpirationDate().toString()));
                member.setMembershipExpiration(exp);
            }

            member.setPrivateBoatId(m.getPrivateBoatSerialNumber());
            member.setPhone(m.getPhoneNumber());
            member.setPassword(m.getPassword());
            member.setManager(m.isManager());

            members.getMember().add(member);
        }

        try {
          return marshalMembers(members);
        } catch (JAXBException e) {
            throw new ExportToXmlException("Failed to save data to XML file.");
        }
    }

     public String exportWindowsToXml() throws ExportToXmlException{
        Activities activities = new Activities();

        for (ScheduleWindow s: this.scheduleWindows){
            Timeframe t = new Timeframe();
            if (s.getBoatType() != null)
                t.setBoatType(BoatType.valueOf(s.getBoatType().toString()));
            t.setName(s.getName());
            t.setStartTime(s.getStartTime().toString());
            t.setEndTime(s.getEndTime().toString());

            activities.getTimeframe().add(t);
        }
        try {
            return marshalActivities(activities);
        } catch (JAXBException e) {
            throw new ExportToXmlException("Failed to save data to XML file.");        }
    }

    public String exportBoatsToXml() throws ExportToXmlException{
        Boats boats = new Boats();
        for (engine.classes.boat.Boat b: this.boats){
            Boat boat = new Boat();
            boat.setId(Integer.toString(b.getID()));
            boat.setName(b.getBoatName());
            boat.setType(BoatType.valueOf(b.getBoatType().toString()));
            boat.setPrivate(b.isPrivateBoat());
            boat.setWide(b.isWideBoat());
            boat.setHasCoxswain(b.hasCoxswain());
            boat.setCostal(b.isCoastal());
            boat.setOutOfOrder(b.isFunctioningBoat());

            boats.getBoat().add(boat);
        }

        try {
            return marshalBoats(boats);
        } catch (JAXBException e) {
            throw new ExportToXmlException("Failed to save data to XML file.");        }
    }
    public XMLGregorianCalendar convertToGregorianCalendar(LocalDate date) throws DatatypeConfigurationException {
        GregorianCalendar gcal = GregorianCalendar.from(date.atStartOfDay(ZoneId.systemDefault()));
        return DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
    }

    public String marshalMembers(Members members) throws JAXBException{
        StringWriter stringWriter = new StringWriter();
        JAXBContext jaxbContext = JAXBContext.newInstance(Members.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,true);
        marshaller.marshal(members, stringWriter);

        return stringWriter.toString();
    }

    public String marshalActivities(Activities activities) throws JAXBException{
        StringWriter stringWriter = new StringWriter();

        JAXBContext jaxbContext = JAXBContext.newInstance(Activities.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,true);
        marshaller.marshal(activities,stringWriter);

        return  stringWriter.toString();

    }

    public String marshalBoats(Boats boats) throws JAXBException {
        StringWriter stringWriter = new StringWriter();

        JAXBContext jaxbContext = JAXBContext.newInstance(Boats.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,true);
        marshaller.marshal(boats,stringWriter);

        return stringWriter.toString();
    }

    public boolean isBookingAssociatedToMoreUsers(Booking theBookingToChange){
        return theBookingToChange.getOtherParticipatingRowersID().size() > 0;
    }

    public List<engine.classes.boat.Boat> getAvailableBoats(int bookingID) {
        List<engine.classes.boat.Boat> res = new ArrayList<>();
        Booking booking = retrieveBookingPerID(bookingID);
        boolean canAdd;
        for (engine.classes.boat.Boat boat : boats) {
            canAdd = true;
            if (!boat.isPrivateBoat() && boat.isFunctioningBoat()) {
                for (Booking b : bookings) {
                    if (b.isApproved()){
                        if (b.getAssignedBoatID() == boat.getID()){
                            if (booking.getRequestedPracticeDate().equals(b.getRequestedPracticeDate())){
                                ScheduleWindow requestedBookingWindow = retrieveScheduleWindowPerID(booking.getRequestedWindowID());
                                ScheduleWindow loopWindow = retrieveScheduleWindowPerID(b.getRequestedWindowID());
                                if (requestedBookingWindow.getStartTime().equals(loopWindow.getStartTime()))
                                    canAdd = false;
                            }
                        }
                    }
                }
            }
            if (canAdd)
                res.add(boat);
        }
        return Collections.unmodifiableList(res);

    }

    private void setMemberCounterID(int max) {
        new engine.classes.member.Member().setCounter(max);
    }

    private void setBookingCounterID(int max) {
        new Booking().setCounter(max);
    }

    private void setBoatCounterID(int max) {
        new engine.classes.boat.Boat().setCounter(max);
    }

    private void setWindowsCounterID(int max) {
        new ScheduleWindow().setCounter(max);
    }

    private void setMaxIDInMembers(List<engine.classes.member.Member> members) {
        List<Integer> membersIDsList = generateMembersIDsList(members);
        int max = getMaxID(membersIDsList);
        setMemberCounterID(max);
    }

    private void setMaxIDInBookings(List<Booking> bookings) {
        List<Integer> bookingsIDsList = generateBookingsIDsList(bookings);
        int max = getMaxID(bookingsIDsList);
        setBookingCounterID(max);
    }

    private List<Integer> generateBookingsIDsList(List<Booking> bookings) {
        List<Integer> res = new ArrayList<>();
        for (Booking b : bookings)
            res.add(b.getBookingID());
        return res;
    }

    private void setMaxIDInWindows(List<ScheduleWindow> windows) {
        List<Integer> windowsIDsList = generateWindowsIDsList(windows);
        int max = getMaxID(windowsIDsList);
        setWindowsCounterID(max);
    }

    private List<Integer> generateWindowsIDsList(List<ScheduleWindow> windows) {
        List<Integer> res = new ArrayList<>();
        for (ScheduleWindow sw : windows)
            res.add(sw.getID());
        return res;
    }

    private void setMaxIDInBoats(List<engine.classes.boat.Boat> boats) {
        List<Integer> boatsIDsList = generateBoatsIDsList(boats);
        int max = getMaxID(boatsIDsList);
        setBoatCounterID(max);

    }

    private List<Integer> generateBoatsIDsList(List<engine.classes.boat.Boat> boats) {
        List<Integer> res = new ArrayList<>();
        for (engine.classes.boat.Boat b : boats)
            res.add(b.getID());
        return res;
    }

    private int getMaxID(List<Integer> listOfIDs) {
        if (listOfIDs.size() == 0)
            return 0;
        return listOfIDs.stream().mapToInt(i -> i).max().orElseThrow(NoSuchElementException::new);
    }

    private List<Integer> generateMembersIDsList(List<engine.classes.member.Member> members) {
        List<Integer> res = new ArrayList<>();
        for (engine.classes.member.Member m : members)
            res.add(m.getID());
        return res;
    }

    public List<engine.classes.member.Member> getMembersListWithoutUser(int bookingID) {
        List<engine.classes.member.Member> res = new ArrayList<>();
        List<Integer> rowersIDs = retrieveBookingPerID(bookingID).getOtherParticipatingRowersID();
        for (engine.classes.member.Member m : members) {
            if (m.getID() != retrieveBookingPerID(bookingID).getMemberOrderedID() && !rowersIDs.contains(m.getID()))
                res.add(m);
        }
        return res;
    }

    @Override
    public List<engine.classes.member.Member> getMembers() {
        return members;
    }

    @Override
    public List<engine.classes.boat.Boat> getBoats() {
        return boats;
    }

    @Override
    public List<Booking> getBookings() {
        return bookings;
    }

    @Override
    public List<ScheduleWindow> getScheduleWindows() {
        return scheduleWindows;
    }

    @Override
    public boolean addToActiveUsersList(int userID) {
        if (isUserActive(userID))
            return false;
        else {
            this.activeUsersIDs.add(userID);
            return true;
        }
    }

    @Override
    public boolean isUserActive(int userID) {
        for (Integer id: this.activeUsersIDs){
            if (userID == id)
                return true;
        }
        return false;
    }

    @Override
    public void logoutUser(int id) {
        activeUsersIDs.removeIf(i -> id == i);
    }

    @Override
    // Generates a map where Level enum is the key, and the frequency of that level in participating rowers is the value.
    public Map<Level, Integer> generateLevelsMapForMembersList(int bookingID) throws SmartAssignmentException{
        List<engine.classes.member.Member> rowersInBooking = retrieveMembersAsListPerID(retrieveBookingPerID(bookingID).getOtherParticipatingRowersID());
        rowersInBooking.add(retrieveMemberPerID(retrieveBookingPerID(bookingID).getMemberOrderedID()));

        if (rowersInBooking.isEmpty())
            throw new SmartAssignmentException("Couldn't find any participating rowers in the booking.");

        Map<Level,Integer> res = new HashMap<>();
        Level[] values = Level.values();
        for (Level level: values)
            res.put(level,0);

        for (engine.classes.member.Member m: rowersInBooking){
            Level memberLevel = m.getLevel();
            if (memberLevel != null){
                res.put(memberLevel,res.get(memberLevel) + 1);
            }
        }
        return res;
    }

    @Override
    public List<engine.classes.boat.Boat> getRelevantBoatsPerRowersMostCommonLevelFrequency(List<Level> commonLevels, List<engine.classes.boat.Boat> availableBoats) {

        return getSuggestedBoatsPerLevel(availableBoats,commonLevels);

    }

    private List<engine.classes.boat.Boat> getSuggestedBoatsPerLevel(List<engine.classes.boat.Boat> availableBoats, List<Level> commonLevels) {
        if (commonLevels.size() == 0)
            return availableBoats;
        List<engine.classes.boat.Boat> res = new ArrayList<>();

        for (Level level : commonLevels){
            for (engine.classes.boat.Boat b: availableBoats){
                if (level.getSymbol().equalsIgnoreCase(Level.BEGINNER.getSymbol())){
                    if (b.isWideBoat())
                        res.add(b);
                }
                else if (level.getSymbol().equalsIgnoreCase(Level.ADVANCED.getSymbol()) || level.getSymbol().equalsIgnoreCase(Level.INTERMEDIATE.getSymbol()))
                    if (!b.isWideBoat())
                        res.add(b);
            }
            if (res.size() > 0)
                return res;

        }
        return res;
    }


    // Returns a list sorted by frequency of levels of the rowers.
    public List<Level> getSortedCommonLevelsList(Map<Level, Integer> participatingRowersLevelsMap) {
        Map<Level,Integer> tmp = new HashMap<>(participatingRowersLevelsMap);
        Level commonLevel;
        List<Level> res = new ArrayList<>();
        while (!tmp.keySet().isEmpty()){
            commonLevel = tmp.entrySet().stream().max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get().getKey();
            res.add(commonLevel);
            tmp.keySet().remove(commonLevel);
        }
        return res;
    }


    public engine.classes.member.Member getMemberPerUsername(String email){
        for (engine.classes.member.Member m: members){
            if (m.getEmailAddress().equalsIgnoreCase(email))
                return m;
        }
        return null;
    }

    @Override
    public List<engine.classes.boat.BoatType> getBoatTypeListFromStringArray(String[] boatTypesAsStringArray) {
        List<engine.classes.boat.BoatType> resList = new ArrayList<>();
        if (boatTypesAsStringArray.length == 0)
            return resList;
        for (int i=0;i<boatTypesAsStringArray.length;i++){
            engine.classes.boat.BoatType type = getBoatTypeFromString(boatTypesAsStringArray[i]);
            if (type != null)
                resList.add(type);
        }
        return resList;
    }

    @Override
    public void removeRowersFromBooking(int bookingID, List<Integer> rowersIDs) {
        Booking booking = retrieveBookingPerID(bookingID);

        for(Integer id: rowersIDs)
            booking.removeParticipatingRower(id);

    }

}
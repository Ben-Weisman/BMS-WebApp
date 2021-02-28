package engine.engine;

import engine.classes.boat.Boat;
import engine.classes.boat.BoatType;
import engine.classes.booking.Booking;
import engine.classes.booking.BookingDetails;
import engine.classes.member.Level;
import engine.classes.member.Member;
import engine.classes.member.MemberDetails;
import engine.classes.windows.ScheduleWindow;
import engine.classes.windows.ScheduleWindowDetails;
import engine.customExceptions.*;

import engine.customExceptions.InvalidInputException;
import engine.generated.Activities;
import engine.generated.Boats;
import engine.generated.Members;
import engine.generated.Timeframe;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public interface BMSEngine {


    /*
     * Uses for login to the system by the user.
     * The function gets from the Console module the emil and password of the user, and then goes to Member list
     * in the engine and search for a match.
     * returns:
     * true - if found a match, meaning the user is logged in.
     * false - if no match was found. Meaning the user isn't logged in and can't access the menu options.
     * */
    public Member accessVerification(String email, String pass);

    // Simple boolean method that returns true if scheduleWindow list is empty. Else return false.
    public boolean isScheduleWindowListEmpty();


    // Lets the user to add a new window as desired (In-case the Window list is empty).
    public ScheduleWindow addNewWindow(ScheduleWindowDetails sw) throws ExportToXmlException, InvalidInputException, JAXBException;


    // Returns the member list in a read-only format.
    public List<Member> getReadOnlyMembersList();


    // Add a newly created booking request to the bookings list.
    public void addNewBookingRequest(BookingDetails bookingToAdd) throws InvalidInputException, JAXBException;


    // Returns the ScheduleWindow list in a read-only format.
    public List<ScheduleWindow> getReadOnlyScheduleWindowList();

    // Returns the BookingRequests list in a read-only format.
    public List<Booking> getReadOnlyBookingRequestsList();

    // Returns the BoatsList list in a read-only format.
    public List<Boat> getReadOnlyBoatsListList();

    // Updates the member name.
    public void updateMemberName(String memberName, String newName) throws InvalidInputException, NotfoundException, ExportToXmlException, JAXBException;

    // updates the member email address.
    public void updateMemberEmail(String memberName, String scannedParam) throws InvalidInputException, NotfoundException, ExportToXmlException, JAXBException;

    // updates the member's password.
    public void updateMemberPassword(String memberName, String scannedParam) throws InvalidInputException, NotfoundException, ExportToXmlException, JAXBException;


    public void updateMemberPhone(String memberName, String scannedParam) throws InvalidInputException, NotfoundException, ExportToXmlException, JAXBException;



    public List<Booking> generateAllFutureBookingRequests(int userID);


    public void addAdminUserForProgramAccess();


    public Booking retrieveBookingPerID(int theID);


    public void editPracticeDate(String newVal, Booking theBookingToChange) throws InvalidInputException, ExportToXmlException, JAXBException;


    public void editBookingWindow(ScheduleWindow theNewWindow, Booking theBookingToChange) throws ExportToXmlException, JAXBException;


    public void editRequestedBoatType(List<BoatType> updatedBoats, Booking theBookingToChange) throws InvalidInputException, JAXBException;


    public void removeBookingRequest(Booking theBookingToChange) throws ExportToXmlException, JAXBException;


    public List<Booking> generateRequestsHistoryOfTheUSer(int userID);

    public boolean isTheUserPartOfTheRequest(Booking b, int userID);


    public List<Booking> getBookingList();

    public List<Booking> getPendingBookingRequests();



    public void saveEngineStateToXML() throws JAXBException;



    public ScheduleWindow retrieveScheduleWindowPerID(int windowID);

    public Member retrieveMemberPerID(int id);

    public List<Member> retrieveMembersAsListPerID(List<Integer> theIDs);

    public void setMembersList(List<Member> otherMembers);

    public void setBookingsList(List<Booking> otherBookings);

    public void setBoatsList(List<Boat> otherBoats);

    public void setScheduleWindowList(List<ScheduleWindow> otherWindows);

    public void marshalEngine(BMSEngine engine, File file) throws JAXBException;

    //boatKind is the boatType symbol, and may contain "coastal" or "Wide as second word
    public void addBoat(String boatKind, String boatName, boolean isPrivate) throws InvalidBoatNameException, InvalidTypeException, JAXBException;

    public BoatType getBoatTypeFromString(String boatStringRepresentation) ;

    public void addMember(MemberDetails member) throws JAXBException;

    void addBookingRequest(Booking booking);

    void addActivity(ScheduleWindow activity);

    void removeBoat(String boatId, boolean forceRemove) throws NotfoundException, ExportToXmlException, NeedToChangeOtherListingException, JAXBException;

    void removeMember(String memberId) throws NotfoundException, ExportToXmlException, JAXBException;

    void removeBookingRequest(String bookingRequestId) throws NotfoundException;

    Boat retrieveBoatPerID(int assignedBoatID);

    void removeScheduleWindow(String scheduleWindowName) throws NotfoundException;

    public void updateScheduleWindowName(String scheduleWindowName, String newName) throws InvalidInputException, NotfoundException;

    public void updateScheduleWindowStartTime(String scheduleWindowName, LocalTime time) throws InvalidInputException, NotfoundException;

    public void updateScheduleWindowEndTime(String scheduleWindowName, LocalTime time) throws InvalidInputException, NotfoundException;

    public void updateScheduleWindowBoatType(String scheduleWindowName, BoatType boatType) throws InvalidInputException, NotfoundException;

    public void loadEngine(File filePath) throws JAXBException;

    public void assignLists(BMSEngine engine);

    public void importBoatsFromXML(boolean runOverCurrentList, String fileAsString) throws ImportXmlException, InvalidBoatNameException, InvalidTypeException;

    Boats deserializeBoatsFrom(String in) throws JAXBException;

    Members deserializeMembersFrom(String in) throws JAXBException;

    Activities deserializeActivitiesFrom(String in) throws JAXBException;

    List<engine.generated.Member> generatedValidImportedMembersList(Members members, List<String> corruptedRecords);

    List<Timeframe> generateValidImportedActivitiesList(Activities activities, List<String> corruptedRecords);

    public void allocateMembersAndReplaceWithCurrentList(List<engine.generated.Member> members);
    void allocateActivitiesAndReplaceWithCurrentList(List<Timeframe> validRecords) throws InvalidInputException;

    void allocateBoatsAndReplaceWithCurrentList(List<engine.generated.Boat> generatedBoats) throws InvalidTypeException, InvalidBoatNameException;

    public void addMembersToCurrentList(List<engine.generated.Member> members);

    boolean isBoatNameExists(String boatName);

    boolean isMemberEmailAddressExistsInList(String email);

    boolean isActivityNameExistsInList(String name);

    Boat createBoat(engine.generated.Boat b) throws InvalidBoatNameException, InvalidTypeException;

    Member createMember(engine.generated.Member m);

    ScheduleWindow createScheduleWindow(Timeframe t) throws InvalidInputException;


    void importMembersFromXML(boolean runOverCurrentList, String fileAsString)throws ImportXmlException;

    void importWindowsFromXML(boolean runOverCurrentList, String fileAsString)throws ImportXmlException,InvalidInputException,IllegalDataInXmlFileException;

    boolean isMemberIDExists(int id);

    void addActivitiesToCurrentList(List<Timeframe> validRecords) throws InvalidInputException;

    boolean isPrivateBoatIDExistsInBoatsList(int idToFind);

     String exportMembersToXml() throws DatatypeConfigurationException,ExportToXmlException;

     String exportWindowsToXml() throws ExportToXmlException;
     String exportBoatsToXml() throws ExportToXmlException;

    XMLGregorianCalendar convertToGregorianCalendar(LocalDate date) throws DatatypeConfigurationException ;

    String marshalMembers(Members members) throws JAXBException;

    String marshalActivities(Activities activities) throws JAXBException;

    String marshalBoats(Boats boats) throws JAXBException;

    boolean isBookingAssociatedToMoreUsers(Booking theBookingToChange);


    void updateMemberAge(String memberName, int ageFromUser) throws InvalidInputException, NotfoundException, JAXBException;

    void updateMemberComments(String memberName, String commentsFromUser) throws InvalidInputException, NotfoundException, JAXBException;

    void updateMemberExpirationDate(String memberName, LocalDate expirationDateFromUser) throws InvalidInputException, NotfoundException, JAXBException;

    void updateMemberHasPrivateBoat(String memberName, boolean fromUserIfHasPrivateBoat) throws InvalidInputException, NotfoundException, JAXBException;

    void updateMemberPrivateBoatSerialNumber(String memberName, String privateBoatSerialNumber) throws InvalidInputException, NotfoundException, JAXBException;

    void updateMemberIsManager(String memberName, boolean isManager) throws InvalidInputException, NotfoundException, JAXBException;

    List<engine.generated.Boat> generateValidImportedBoatsList(List<engine.generated.Boat> boatsParam, List<String> corruptedRecords);

    void updateBoatName(String boatIDToUpdate, String nameFromUser) throws InvalidInputException, NotfoundException, JAXBException;

    void updateBoatType(String boatIDToUpdate, BoatType boatTypeFromUser) throws InvalidInputException, NotfoundException, JAXBException;

    void updateBoatIsPrivate(String boatIDToUpdate, boolean isPrivate) throws InvalidInputException, NotfoundException, JAXBException;

    void updateBoatIsWide(String boatIDToUpdate, boolean isWide) throws InvalidInputException, NotfoundException, JAXBException;

    void updateBoatHasCoxswain(String boatIDToUpdate, boolean hasCoxswain) throws InvalidInputException, NotfoundException, JAXBException;

    void updateBoatIsCoastal(String boatIDToUpdate, boolean isCoastal) throws InvalidInputException, NotfoundException, JAXBException;

    void updateBoatIsFunctioning(String boatIDToUpdate, boolean isFunctioning) throws InvalidInputException, NotfoundException, JAXBException;

    void updateBookingPracticeDate(String bookingIDToUpdate, LocalDate practiceDate) throws InvalidInputException, NotfoundException, JAXBException;

    void updateBookingRequestedWindowID(String bookingIDToUpdate, String requestedWindowID) throws InvalidInputException, NotfoundException, JAXBException;

    void updateBookingAssignedBoatID(int bookingIDToUpdate, int assignedBoatID) throws BoatAssignmentException, InvalidInputException, NotfoundException, JAXBException;

    void updateBookingRequestedBoatTypes(String bookingIDToUpdate, List<BoatType> boatTypesFromUser) throws InvalidInputException, NotfoundException, JAXBException;

    void updateBookingOtherParticipatingRowersID(String bookingIDToUpdate, List<Integer> participatingRowersIDs) throws InvalidInputException, NotfoundException, JAXBException;

    List<Boat> getAvailableBoats(int bookingID);

    List<Member> getMembersListWithoutUser(int bookingID);

    List<Member> getMembers();
    List<Boat> getBoats();
    List<Booking> getBookings();
    List<ScheduleWindow> getScheduleWindows();

    public boolean addToActiveUsersList(int userID);

    public boolean isUserActive(int userID);

    void logoutUser(int id);

    List<Level> getSortedCommonLevelsList(Map<Level, Integer> participatingRowersLevelsMap);

    Map<Level, Integer> generateLevelsMapForMembersList(int bookingID) throws SmartAssignmentException;

    List<Boat> getRelevantBoatsPerRowersMostCommonLevelFrequency(List<Level> commonLevels, List<Boat> availableBoats);

    public Member getMemberPerUsername(String email);

    List<BoatType> getBoatTypeListFromStringArray(String[] boatTypesAsStringArray);

    void removeRowersFromBooking(int bookingID, List<Integer> rowersIDs);

}
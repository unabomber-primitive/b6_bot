package main.database;

import main.core.*;
import main.utils.Transformation;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

@Component
public class DatabaseManager {
    public static boolean insertIntoCompletedTasks(String phoneNumber, Integer taskId) {
        int n = 0;
        Long employeeId = getEmployeeId(phoneNumber);
        try(Connection connection = DataSource.getConnection()) {
            String sql = "insert into completed_tasks (completed_task_id, employee_id) values (?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, taskId);
            ps.setLong(2, employeeId);

            n = ps.executeUpdate();
        }
        catch(SQLException e) {
            e.getMessage();
        }
        return n > 0;
    }

    private static Long getEmployeeId(String phoneNumber) {
        try(Connection connection = DataSource.getConnection()) {
            String sql = "select employee_id from employees where phone_number = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, phoneNumber);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getLong("employee_id");
        }
        catch(SQLException e) {
            e.getMessage();
        }
        return null;
    }
    public static boolean isCardBlocked(String userId) {
        Integer cardNumber = getCardNumber(userId);
        try(Connection connection = DataSource.getConnection()) {
            String sql = "select state from cards where card_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, cardNumber);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getBoolean("state");
        }
        catch(SQLException e) {
            e.getMessage();
        }
        return false;
    }

    public static boolean addNews(String userId, String header, String news) {
        int n = 0;
        try(Connection connection = DataSource.getConnection()) {
            String sql = "insert into news (header, description, creator_id, date) values (?, ?, ?, CURRENT_DATE)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, header);
            ps.setString(2, news);
            ps.setLong(3, Long.parseLong(userId));
            n = ps.executeUpdate();
        }
        catch(SQLException e) {
            e.getMessage();
        }
        return n > 0;
    }

    public static String chatIdByCardNumber(Integer cardNumber) {
        try(Connection connection = DataSource.getConnection()) {
            String sql = "select student_id from students where card_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, cardNumber);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return String.valueOf(rs.getLong("student_id"));
        }
        catch(SQLException e) {
            e.getMessage();
        }
        return null;
    }

    public static boolean confirmApplications() {
        int n = 0;
        try(Connection connection = DataSource.getConnection()) {
            String sql = """
                    update applications
                    set confirmation = true
                    where confirmation = false""";
            PreparedStatement ps = connection.prepareStatement(sql);
            n = ps.executeUpdate();
        }
        catch(SQLException e) {
            e.getMessage();
        }
        return n > 0;
    }

    public static boolean hasRights(String userId, String role) {
        //System.out.println("KEEEEK");
        String realRole = "";
        try(Connection connection = DataSource.getConnection()) {
            String sql = "select employees.position from employees\n" +
                    "where employees.employee_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, Long.parseLong(userId));
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
               realRole  = rs.getString("position");
            }
        }
        catch(SQLException e) {
            e.getMessage();
        }

        return realRole.equals(role);
    }

    public static ArrayList<Application> getApplicationWaitingForConfirmation() {
        try(Connection connection = DataSource.getConnection()) {
            String sql = """
                    select a.card_number, a.date, a.start_time, a.end_time, g.name, g.surname, g.middle_name,
                    g.passport_seria, g.passport_number, g.passport_organization, g.passport_date, g.passport_division_code from applications a
                    join guests g on g.guest_id = a.guest_id
                    where a.confirmation = false;
                    """;
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            return Transformation.transformApplications(rs);
        }
        catch(SQLException e) {
            e.getMessage();
        }

        return new ArrayList<>();
    }

    public static ArrayList<EmployeeTask> getTasks() {
        try(Connection connection = DataSource.getConnection()) {
            String sql = """
                    select t.problem_description, t.chosen_time, t.card_number, students.room_id, services.employee_position from tasks t
                    join students on students.card_id = t.card_number
                    join services on services.service_id = t.service_id;
                        """;
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            return Transformation.transformEmployeeTask(rs);
        }
        catch(SQLException e) {
            e.getMessage();
        }

        return null;
    }

    public static ArrayList<DeparturePersonInfo> getWhoHasLeft() {
        try(Connection connection = DataSource.getConnection()) {
            String sql = """
                    select students.room_id, departures.card_number, departures.date from departures
                    join students on students.card_id = departures.card_number
                    where departures.date = CURRENT_DATE;""";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            return Transformation.transformDeparturePersonInfo(rs);
        }
        catch(SQLException e) {
            e.getMessage();
        }

        return new ArrayList<>();
    }

    public static String isStudentOrEmployee(String userId) {
        if (isEmployee(userId)) return "Сотрудник";
        if (isStudent(userId)) return "Студент";
        return null;
    }

    private static boolean isStudent(String userId) {
        String sqlStudent = "select * from students where student_id = ?";
        System.out.println(userId);
        return hasUser(userId, sqlStudent, "student_id") != null;
    }

    private static boolean isEmployee(String userId) {
        String sqlEmployee = "select * from employees where employee_id = ?";
        return hasUser(userId, sqlEmployee, "employee_id") != null;
    }

    private static String hasUser(String userId, String sql, String columnName) {
        long n = -1;
        try(Connection connection = DataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, Long.parseLong(userId));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) n = rs.getLong(columnName);
        }
        catch(SQLException e) {
            e.getMessage();
        }
        return n > -1 ? "exist" : null;
    }

    public static boolean createBooking(String userId, String roomId, String date, String hour) {
        int n = 0;
        Integer cardNumber = getCardNumber(userId);
        try(Connection connection = DataSource.getConnection()) {
            String sql = "insert into bookings (room_id, card_number, start_time) values (?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(roomId));
            ps.setInt(2, cardNumber);
            ps.setTimestamp(3, Timestamp.valueOf(date + " " + hour));
            System.out.println(n);
            n = ps.executeUpdate();
        }
        catch(SQLException e) {
            e.getMessage();
        }
        return n > 0;
    }

    public static boolean createTask(String userId, String serviceId, String problemDescription, String date, String hour) {
        int n = 0;
        Integer cardNumber = getCardNumber(userId);
        try(Connection connection = DataSource.getConnection()) {
            String sql = "insert into tasks (service_id, card_number, problem_description, chosen_time, creation_time) values (?, ?, ?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(serviceId));
            ps.setInt(2, cardNumber);
            ps.setString(3, problemDescription);
            ps.setTimestamp(4, Timestamp.valueOf(date + " " + hour));
            ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            System.out.println(n);
            n = ps.executeUpdate();
        }
        catch(SQLException e) {
            e.getMessage();
        }
        return n > 0;
    }

    public static HashMap<String, Room> getRooms() {
        try(Connection connection = DataSource.getConnection()) {
            String sql = """
                    select rooms.room_id, room_types.type from rooms
                    join room_types on room_types.type_id = rooms.type_id
                    where rooms.type_id != 2 and rooms.type_id != 7;""";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            return Transformation.transformRooms(rs);
        }
        catch(SQLException e) {
            e.getMessage();
        }

        return null;
    }

    public static boolean createDepartureApplication(String userId, String date) {
        int n = 0;
        try(Connection connection = DataSource.getConnection()) {
            Integer cardNumber = getCardNumber(userId);
            String sql = "insert into departures (card_number, date) values (?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1,cardNumber);
            ps.setDate(2, Date.valueOf(date));
            n = ps.executeUpdate();
        }
        catch (SQLException e) {
            e.getMessage();
        }
        return n > 0;
    }



    public static ArrayList<Task> getRequests(String userId) {
        try(Connection connection = DataSource.getConnection()) {
            Integer cardNumber = getCardNumber(userId);
            String sql = "select * from tasks where card_number = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1,cardNumber);
            ResultSet rs = ps.executeQuery();
            return Transformation.transformRequests(rs);
        }
        catch(SQLException e) {
            e.getMessage();
        }

        return new ArrayList<>();
    }

    private static Integer getCardNumber(String userId) {
        try(Connection connection = DataSource.getConnection()) {
            String sql = "select card_id from students where (student_id = ?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, Long.parseLong(userId));
            ResultSet res = ps.executeQuery();
            res.next();
            return res.getInt("card_id");
        }
        catch (SQLException e) {
            e.getMessage();
        }

        return null;
    }

    public static HashMap<String, Service> getServices() {
        try(Connection connection = DataSource.getConnection()) {
            String sql = "select service_id, name from services";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            return Transformation.transformServices(rs);
        }
        catch (SQLException e) {
            e.getMessage();
        }

        return null;
    }

    public static boolean createGuestApplication(String userId, String[] params) {
        Integer guestId = getGuestId(params[1].split(" "));

        if (guestId == null) guestId = createGuest(params);

        return createApplication(userId, guestId, params);
    }

    private static Integer getGuestId(String[] passport) {
        Integer id = null;
        try(Connection connection = DataSource.getConnection()) {
            String query = "select guest_id from guests where (passport_seria = ? and passport_number = ?)";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, passport[0]);
            ps.setString(2, passport[1]);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                id = rs.getInt("guest_id");
            }
        }
        catch (SQLException e) {
            e.getMessage();
        }

        return id;
    }

    private static Integer createGuest(String[] guestData) {
        String[] name = guestData[0].split(" ");
        String[] passport = guestData[1].split(" ");

        try(Connection connection = DataSource.getConnection()) {
            String query = "insert into guests (name, surname, middle_name, passport_seria, passport_number, passport_organization, passport_date, passport_division_code) values" +
                    "(?, ?, ?, ?, ?, ?, '?', ?)";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, name[0]);
            ps.setString(2, name[1]);
            ps.setString(3, name[2]);
            ps.setString(4, passport[0]);
            ps.setString(5, passport[1]);
            ps.setString(6, guestData[2]);
            ps.setDate(7, Date.valueOf(guestData[3]));
            ps.setString(8, guestData[4]);

            ps.executeUpdate();
        }
        catch (SQLException e) {
            e.getMessage();
        }

        return getGuestId(passport);
    }

    private static boolean createApplication(String userId, Integer guestId, String[] params) {
        int n = 0;
        Integer cardNumber = getCardNumber(userId);
        try(Connection connection = DataSource.getConnection()) {
            String query = "insert into applications (card_number, guest_id, confirmation, date, start_time, end_time) values" +
                    "(?, ?, ?, '2024-02-03', '2024-02-04 10:00:00', '2024-02-04 16:00:00')";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, cardNumber);
            ps.setInt(2, guestId);
            ps.setBoolean(3, false);
            ps.setTimestamp(4, Timestamp.valueOf(params[5]));
            ps.setTime(5, Time.valueOf(params[6]));
            ps.setTime(6, Time.valueOf(params[7]));
            n  = ps.executeUpdate();
        }
        catch (SQLException e) {
            e.getMessage();
        }

        return n > 0;
    }
}

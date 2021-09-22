package edu.miu.cs.appointmentsystem.domain.helpers;

public abstract class Constants {
    public abstract static class Roles {
        public static final String ADMIN = "ADMIN";
        public static final String PROVIDER = "PROVIDER";
        public static final String CLIENT = "CLIENT";
    }

    public abstract static class Appointment {
        public static final String TABLE_NAME = "Appointments";
        // Field Properties
        public static final String DATE_ASC_TIME_ASC = "createdDate asc";
        public static final String APPOINTMENT = "appointment";
        public static final String COLUMN_NAME_PROVIDER_ID = "provider_id";
        public static final String COLUMN_NAME_CATEGORY_ID = "category_id";
    }

    public abstract static class Reservation {
        public static final String TABLE_NAME = "Reservations";
        // Field Properties
        public static final String APPOINTMENT_ID = "appointment_id";
    }

    public abstract static class BaseEntity {
        // Field Properties
        public static final String CREATED_BY_ID = "created_by_id";
        public static final String UPDATED_BY_ID = "updated_by_id";
    }

    public abstract static class User {
        public static final String TABLE_NAME = "Users";
        // Field Properties
    }

    public abstract static class UserRole {
        public static final String TABLE_NAME = "UserRoles";
        // Field Properties
    }

    public abstract static class EmailServiceLog {
        public static final String TABLE_NAME = "EmailServiceLogs";
        // Field Properties
    }

    public abstract static class Provider {
        public static final String TABLE_NAME = "Providers";
        // Field Properties
    }

    public abstract static class Category {
        public static final String TABLE_NAME = "Categories";
        // Field Properties
    }
}

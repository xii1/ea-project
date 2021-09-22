package edu.miu.cs.appointmentsystem.controllers.helper;

public abstract class RestConstants {

    public static final String SINGLE = "/{id}";
    public abstract static class Category {
        public static final String PREFIX = "/categories";
        public static final String ID_PARAM_NAME = "cId";
        public static final String SINGLE = "/{" + ID_PARAM_NAME + "}";

        public abstract static class Provider {
            public static final String ID_PARAM_NAME = "pId";
            public static final String SINGLE = "/{" + ID_PARAM_NAME + "}";
            public static final String PREFIX = RestConstants.Category.PREFIX + RestConstants.Category.SINGLE
                    + "/providers";
            public static final String PREFIX_PURE = "/providers";

            public abstract static class User {
                public static final String ID_PARAM_NAME = "uId";
                public static final String SINGLE = "/{" + ID_PARAM_NAME + "}";
                public static final String PREFIX = RestConstants.Category.Provider.SINGLE + "/users";

                public static final String REMOVE_USER = RestConstants.Category.Provider.User.PREFIX
                        + RestConstants.Category.Provider.User.SINGLE;

                public static final String UPDATE_USER = RestConstants.Category.Provider.User.PREFIX
                        + RestConstants.Category.Provider.User.SINGLE;
            }

            public abstract static class Appointment {
                public static final String ID_PARAM_NAME = "aId";
                public static final String SINGLE = "/{" + ID_PARAM_NAME + "}";
                public static final String PREFIX = RestConstants.Category.Provider.PREFIX
                        + RestConstants.Category.Provider.SINGLE + "/appointments";

                public abstract static class Reservation {
                    public static final String PREFIX = RestConstants.Category.Provider.Appointment.PREFIX
                            + RestConstants.Category.Provider.Appointment.SINGLE + "/reservations";
                }
            }
        }
    }

    public abstract static class Authorization {
        public static final String HAS_ANY_AUTHORITY_ADMIN_PROVIDER_CLIENT = "hasAnyAuthority('ADMIN','PROVIDER', 'CLIENT')";
        public static final String HAS_ANY_AUTHORITY_ADMIN_CLIENT = "hasAnyAuthority('ADMIN', 'CLIENT')";
        public static final String HAS_ANY_AUTHORITY_ADMIN_PROVIDER = "hasAnyAuthority('ADMIN','PROVIDER')";
        public static final String HAS_AUTHORITY_ADMIN = "hasAnyAuthority('ADMIN')";

    }

    public abstract static class Authentication {
        public static final String PREFIX = "auth";
        public static final String CHECK_EXISTS_EMAIL = "/exists/{email}";
        public static final String REGISTER = "/register";
        public static final String LOGIN = "/login";
    }

}

package net.aeroclient.client.utils.imgur;

public class WebException extends RuntimeException {
        private StatusCode code;

        /**
         * Creates a new instance of WebException containing
         * the StatusCode and the original exception.
         *
         * @param code
         *          The StatusCode related to this exception.
         * @param cause
         *          The Throwable that set off this exception.
         */
    public WebException(StatusCode code, Throwable cause)
        {
            super(cause);
            this.code = code;
        }

        /**
         * Creates a new instance of WebException containing the StatusCode.
         *
         * @param code
         *          The StatusCode related to this exception.
         */
    public WebException(StatusCode code)
        {
            this(code, null);
        }

        /**
         * Creates a new instance of WebException containing
         * the StatusCode(based on the provided httpCode) and the original exception.
         *
         * @param httpCode
         *          The httpCode related to this exception.
         * @param cause
         *          The Throwable that set off this exception.
         */
    public WebException(int httpCode, Throwable cause)
        {
            this(StatusCode.getStatus(httpCode), cause);
        }

        /**
         * Creates a new instance of WebException containing
         * the StatusCode(based on the provided httpCode).
         *
         * @param httpCode
         *          The httpCode related to this exception.
         */
    public WebException(int httpCode)
        {
            this(httpCode, null);
        }

        /**
         * Gets the Http StatusCode associated with this exception.
         *
         * @return
         *          The StatusCode that caused the exception.
         */
        public StatusCode getStatusCode()
        {
            return code;
        }

        /**
         * Gets the description of the exception based on the description of the StatusCode.
         *
         * @return
         *          Description of exception based on Http StatusCode.
         */
        @Override
        public String getMessage()
        {
            return code.getDescription();
        }
    }

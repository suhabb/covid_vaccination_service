package uk.ac.kcl.covid.vaccine.exception.error;

public class ErrorDto {


    private String status;
    private String title;
    private String description;

    public ErrorDto(){}


    public String getStatus() {
        return status;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    private ErrorDto(Builder builder) {

        this.status = builder.status;
        this.title = builder.title;
        this.description = builder.description;
    }

    public static class Builder {

        public String status;
        public String title;
        public String description;

        public Builder setStatus(String status) {
            this.status = status;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }


        public ErrorDto build() {
            return new ErrorDto(this);
        }
    }
}

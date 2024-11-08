// --- Presentation Layer ---
class Employer {
    private String name;
    private JobListingService jobListingService;

    public Employer(String name, JobListingService jobListingService) {
        this.name = name;
        this.jobListingService = jobListingService;
    }

    public void postJobListing(String title, String description, String skills, String jobType, String location, double salary) {
        System.out.println(name + " is navigating to 'Post a Job' page.");

        JobListing jobListing = JobListingFactory.createJobListing(title, description, skills, jobType, location, salary);
        jobListingService.postJob(jobListing, this);
    }
}

// --- Business Logic Layer ---
class JobListing {
    private String title;
    private String description;
    private String skills;
    private String jobType;
    private String location;
    private double salary;

    public JobListing(String title, String description, String skills, String jobType, String location, double salary) {
        this.title = title;
        this.description = description;
        this.skills = skills;
        this.jobType = jobType;
        this.location = location;
        this.salary = salary;
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getSkills() { return skills; }
    public String getJobType() { return jobType; }
    public String getLocation() { return location; }
    public double getSalary() { return salary; }
}

class JobListingService {
    private SystemService systemService;

    public JobListingService(SystemService systemService) {
        this.systemService = systemService;
    }

    public void postJob(JobListing jobListing, Employer employer) {
        systemService.displayJobPostingForm();
        systemService.previewJobListing(jobListing);

        if (systemService.validateJobListing(jobListing)) {
            systemService.saveJobListing(jobListing);
            systemService.confirmJobListingCreation(employer);
            systemService.makeJobListingVisibleToJobSeekers(jobListing);
        } else {
            systemService.displayErrorMessage();
        }
    }
}

// --- Data Access Layer ---
interface JobListingDAO {
    void save(JobListing jobListing);
}

class JobListingDAOImpl implements JobListingDAO {
    @Override
    public void save(JobListing jobListing) {
        System.out.println("Saving job listing to database: " + jobListing.getTitle());
    }
}

// --- Utility Factory ---
class JobListingFactory {
    public static JobListing createJobListing(String title, String description, String skills, String jobType, String location, double salary) {
        return new JobListing(title, description, skills, jobType, location, salary);
    }
}

// --- Singleton System Service ---
class SystemService {
    private static SystemService instance;
    private JobListingDAO jobListingDAO;

    private SystemService() {
        this.jobListingDAO = new JobListingDAOImpl();
    }

    public static synchronized SystemService getInstance() {
        if (instance == null) {
            instance = new SystemService();
        }
        return instance;
    }

    public void displayJobPostingForm() {
        System.out.println("Displaying job posting form.");
    }

    public void previewJobListing(JobListing jobListing) {
        System.out.println("Displaying job preview for '" + jobListing.getTitle() + "'.");
    }

    public boolean validateJobListing(JobListing jobListing) {
        return jobListing.getTitle() != null && !jobListing.getTitle().isEmpty()
                && jobListing.getDescription() != null && !jobListing.getDescription().isEmpty();
    }

    public void saveJobListing(JobListing jobListing) {
        jobListingDAO.save(jobListing);
    }

    public void confirmJobListingCreation(Employer employer) {
        System.out.println("Job listing creation confirmed for " + employer + ".");
    }

    public void makeJobListingVisibleToJobSeekers(JobListing jobListing) {
        System.out.println("Making job listing visible to job seekers.");
    }

    public void displayErrorMessage() {
        System.out.println("Job details are invalid. Displaying error message.");
    }
}

// --- Main Program ---
public class Main {
    public static void main(String[] args) {
        SystemService systemService = SystemService.getInstance();
        JobListingService jobListingService = new JobListingService(systemService);

        Employer employer = new Employer("John Doe", jobListingService);
        employer.postJobListing("Software Engineer", "Responsible for developing applications.", "Java, Spring Boot", "Full-Time", "Remote", 85000.00);
    }
}

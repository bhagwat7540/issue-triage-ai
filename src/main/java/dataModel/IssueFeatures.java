package dataModel;

//LLM extracted
public class IssueFeatures {
    public String category;          // outage, bug, request
    public String affectedUsers;     // single, multiple, all
    public String component;         // payments, auth, etc.
    public String urgencyHint;        // low, medium, high
}


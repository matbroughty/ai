package com.broughty.ai;

public record Client(int id, String name, String reference, String contactFirstName, String contactSecondName,
                     String emailAddress, String clientManagerEmail, int missingExtractDays, boolean earlyWarningCheck) {

    /**
     * Generates a human-readable string representation of this client.
     *
     * @return A multi-line string containing the client's ID, name, reference, contact details, email address, client manager email, number of days until an extract is considered missing, and whether an early warning check is enabled.
     */
    @Override
    public String toString() {
        return String.format("OA Client Details.  ID: %d\nName: %s\nReference: %s\nContact: %s %s\nEmail: %s\nManager Email: %s\nMissing Extract Days: %d\nEarly Warning Check: %s",
                id, name, reference, contactFirstName, contactSecondName, emailAddress, clientManagerEmail, missingExtractDays, earlyWarningCheck ? "Yes" : "No");
    }
}
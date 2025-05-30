CREATE TABLE IF NOT EXISTS Flights (
                                       id INTEGER PRIMARY KEY AUTOINCREMENT,
                                       destination TEXT NOT NULL,
                                       time_of_departure TIMESTAMP NOT NULL,
                                       airport_name TEXT NOT NULL,
                                       available_seats INTEGER NOT NULL
);
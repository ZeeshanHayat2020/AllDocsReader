package com.furestic.office.ppt.lxs.docx.pdf.viwer.reader.free.pdfViewerModule;

import android.text.TextUtils;

import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Calendar;

public class UtilsPdfViewer {
    public static String parseFileSize(long fileSize) {
        final double kb = fileSize / 1000;

        if (kb == 0) {
            return fileSize + " Bytes";
        }

        final DecimalFormat format = new DecimalFormat("#.##");
        format.setRoundingMode(RoundingMode.CEILING);

        if (kb < 1000) {
            return format.format(kb) + " kB (" + fileSize + " Bytes)";
        }
        return format.format(kb / 1000) + " MB (" + fileSize + " Bytes)";
    }

    // Parse date as per PDF spec (complies with PDF v1.4 to v1.7)
    public static String parseDate(String date) throws ParseException {
        int position = 0;

        // D: prefix is optional for PDF < v1.7; required for PDF v1.7
        if (!date.startsWith("D:")) {
            date = "D:" + date;
        }
        if (date.length() < 6 || date.length() > 23) {
            throw new ParseException("Invalid datetime length", position);
        }

        final Calendar calendar = Calendar.getInstance();
        final int currentYear = calendar.get(Calendar.YEAR);
        int year;

        // Year is required
        String field = date.substring(position += 2, 6);
        if (!TextUtils.isDigitsOnly(field)) {
            throw new ParseException("Invalid year", position);
        }
        year = Integer.valueOf(field);
        if (year > currentYear) {
            year = currentYear;
        }

        position += 4;

        // Default value for month and day shall be 1 (calendar month starts at 0 in Java 7),
        // all others default to 0
        int month = 0;
        int day = 1;
        int hours = 0;
        int minutes = 0;
        int seconds = 0;

        // All succeeding fields are optional, but each preceding field must be present
        if (date.length() > 8) {
            field = date.substring(position, 8);
            if (!TextUtils.isDigitsOnly(field)) {
                throw new ParseException("Invalid month", position);
            }
            month = Integer.valueOf(field) - 1;
            if (month > 11) {
                throw new ParseException("Invalid month", position);
            }
            position += 2;
        }
        if (date.length() > 10) {
            field = date.substring(8, 10);
            if (!TextUtils.isDigitsOnly(field)) {
                throw new ParseException("Invalid day", position);
            }
            day = Integer.valueOf(field);
            if (day > 31) {
                throw new ParseException("Invalid day", position);
            }
            position += 2;
        }
        if (date.length() > 12) {
            field = date.substring(10, 12);
            if (!TextUtils.isDigitsOnly(field)) {
                throw new ParseException("Invalid hours", position);
            }
            hours = Integer.valueOf(field);
            if (hours > 23) {
                throw new ParseException("Invalid hours", position);
            }
            position += 2;
        }
        if (date.length() > 14) {
            field = date.substring(12, 14);
            if (!TextUtils.isDigitsOnly(field)) {
                throw new ParseException("Invalid minutes", position);
            }
            minutes = Integer.valueOf(field);
            if (minutes > 59) {
                throw new ParseException("Invalid minutes", position);
            }
            position += 2;
        }
        if (date.length() > 16) {
            field = date.substring(14, 16);
            if (!TextUtils.isDigitsOnly(field)) {
                throw new ParseException("Invalid seconds", position);
            }
            seconds = Integer.valueOf(field);
            if (seconds > 59) {
                throw new ParseException("Invalid seconds", position);
            }
            position += 2;
        }


        if (date.length() > position) {
            int offsetHours = 0;
            int offsetMinutes = 0;

            final char utRel = date.charAt(position);
            if (utRel != '\u002D' && utRel != '\u002B' && utRel != '\u005A') {
                throw new ParseException("Invalid UT relation", position);
            }

            position++;

            if (date.length() > position + 2) {
                field = date.substring(position, position + 2);
                if (!TextUtils.isDigitsOnly(field)) {
                    throw new ParseException("Invalid UTC offset hours", position);
                }
                offsetHours = Integer.parseInt(field);
                final int offsetHoursMinutes = offsetHours * 100 + offsetMinutes;

                // Validate UTC offset (UTC-12:00 to UTC+14:00)
                if ((utRel == '\u002D' && offsetHoursMinutes > 1200) ||
                        (utRel == '\u002B' && offsetHoursMinutes > 1400)) {
                    throw new ParseException("Invalid UTC offset hours", position);
                }

                position += 2;

                // Apostrophe shall succeed HH and precede mm
                if (date.charAt(position) != '\'') {
                    throw new ParseException("Expected apostrophe", position);
                }

                position++;

                if (date.length() > position + 2) {
                    field = date.substring(position, position + 2);
                    if (!TextUtils.isDigitsOnly(field)) {
                        throw new ParseException("Invalid UTC offset minutes", position);
                    }
                    offsetMinutes = Integer.parseInt(field);
                    if (offsetMinutes > 59) {
                        throw new ParseException("Invalid UTC offset minutes", position);
                    }
                    position += 2;
                }

                // Apostrophe shall succeed mm
                if (date.charAt(position) != '\'') {
                    throw new ParseException("Expected apostrophe", position);
                }
            }


            switch (utRel) {
                case '\u002D':
                    hours -= offsetHours;
                    minutes -= offsetMinutes;
                    break;
                case '\u002B':
                    hours += offsetHours;
                    minutes += offsetMinutes;
                    break;
                default:
                    // "Z" means equal to UTC
                    break;
            }
        }

        calendar.set(year, month, day, hours, minutes, seconds);

        return DateFormat
                .getDateTimeInstance(DateFormat.DEFAULT, DateFormat.LONG)
                .format(calendar.getTime());
    }
}

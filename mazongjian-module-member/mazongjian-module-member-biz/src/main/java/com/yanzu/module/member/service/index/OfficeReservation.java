package com.yanzu.module.member.service.index;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OfficeReservation {
    private List<TimeRange> disabledTimeRanges = new ArrayList<>();
    private List<TimeRange> bookedTimeRanges = new ArrayList<>();

    public void addDisabledTimeRange(TimeRange timeRange) {
        disabledTimeRanges.add(timeRange);
    }

    public void addBookedTimeRange(TimeRange timeRange) {
        bookedTimeRanges.add(timeRange);
    }

    public Map<LocalDate, List<TimeRange>> getUnavailableTimeRanges() {
        Map<LocalDate, List<TimeRange>> unavailableTimeRangesByDay = new HashMap<>();

        // 获取从当前时间起到后5天的日期列表
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(5);

        for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {
            LocalDateTime startDateTime = LocalDateTime.of(date, LocalTime.MIDNIGHT);
            LocalDateTime endDateTime = LocalDateTime.of(date.plusDays(1), LocalTime.MIDNIGHT);

            List<TimeRange> unavailableTimeRanges = calculateUnavailableTimeRanges(startDateTime, endDateTime);
            if (!unavailableTimeRanges.isEmpty()) {
                unavailableTimeRangesByDay.put(date, unavailableTimeRanges);
            }
        }

        return unavailableTimeRangesByDay;
    }

    private List<TimeRange> calculateUnavailableTimeRanges(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        List<TimeRange> unavailableTimeRanges = new ArrayList<>();

        for (TimeRange disabledTimeRange : disabledTimeRanges) {
            if (disabledTimeRange.getStart().toLocalTime().isBefore(disabledTimeRange.getEnd().toLocalTime())) {
                // 禁用时间范围不跨越两天
                LocalDateTime disabledStartDateTime = startDateTime.with(disabledTimeRange.getStart().toLocalTime());
                LocalDateTime disabledEndDateTime = startDateTime.with(disabledTimeRange.getEnd().toLocalTime());

                if (disabledEndDateTime.isAfter(startDateTime) && disabledStartDateTime.isBefore(endDateTime)) {
                    List<TimeRange> bookedTimeRangesForDate = getBookedTimeRangesForDate(startDateTime.toLocalDate());

                    TimeRange intersection = calculateIntersection(disabledStartDateTime, disabledEndDateTime, bookedTimeRangesForDate);
                    if (intersection != null) {
                        unavailableTimeRanges.add(intersection);
                    }
                }
            } else {
                // 禁用时间范围跨越两天
                LocalDateTime disabledStartDateTime1 = startDateTime.with(disabledTimeRange.getStart().toLocalTime());
                LocalDateTime disabledEndDateTime1 = startDateTime.with(LocalTime.MAX);
                LocalDateTime disabledStartDateTime2 = startDateTime.plusDays(1).with(LocalTime.MIDNIGHT);
                LocalDateTime disabledEndDateTime2 = startDateTime.plusDays(1).with(disabledTimeRange.getEnd().toLocalTime());

                if ((disabledEndDateTime1.isAfter(startDateTime) && disabledStartDateTime1.isBefore(endDateTime))
                        || (disabledEndDateTime2.isAfter(startDateTime) && disabledStartDateTime2.isBefore(endDateTime))) {
                    List<TimeRange> bookedTimeRangesForDate1 = getBookedTimeRangesForDate(startDateTime.toLocalDate());
                    List<TimeRange> bookedTimeRangesForDate2 = getBookedTimeRangesForDate(startDateTime.plusDays(1).toLocalDate());

                    TimeRange intersection1 = calculateIntersection(disabledStartDateTime1, disabledEndDateTime1, bookedTimeRangesForDate1);
                    if (intersection1 != null) {
                        unavailableTimeRanges.add(intersection1);
                    }

                    TimeRange intersection2 = calculateIntersection(disabledStartDateTime2, disabledEndDateTime2, bookedTimeRangesForDate2);
                    if (intersection2 != null) {
                        unavailableTimeRanges.add(intersection2);
                    }
                }
            }
        }

        return unavailableTimeRanges;
    }

    private List<TimeRange> getBookedTimeRangesForDate(LocalDate date) {
        List<TimeRange> bookedTimeRangesForDate = new ArrayList<>();

        for (TimeRange bookedTimeRange : bookedTimeRanges) {
            LocalDateTime startDateTime = bookedTimeRange.getStart();
            LocalDateTime endDateTime = bookedTimeRange.getEnd();

            if (startDateTime.toLocalDate().isEqual(date) && endDateTime.toLocalDate().isEqual(date)) {
                bookedTimeRangesForDate.add(bookedTimeRange);
            }
        }

        return bookedTimeRangesForDate;
    }

    private TimeRange calculateIntersection(LocalDateTime startDateTime, LocalDateTime endDateTime, List<TimeRange> bookedTimeRanges) {
        for (TimeRange bookedTimeRange : bookedTimeRanges) {
            LocalDateTime bookedStartDateTime = bookedTimeRange.getStart();
            LocalDateTime bookedEndDateTime = bookedTimeRange.getEnd();

            LocalDateTime intersectionStart = startDateTime.isAfter(bookedStartDateTime) ? startDateTime : bookedStartDateTime;
            LocalDateTime intersectionEnd = endDateTime.isBefore(bookedEndDateTime) ? endDateTime : bookedEndDateTime;

            if (intersectionStart.isBefore(intersectionEnd)) {
                return new TimeRange(intersectionStart, intersectionEnd);
            }
        }

        return null;
    }
}

class TimeRange {
    private LocalDateTime start;
    private LocalDateTime end;

    public TimeRange(LocalDateTime start, LocalDateTime end) {
        this.start = start;
        this.end = end;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }
}

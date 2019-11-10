package example.infrastructure.datasource.timerecord;

import example.application.repository.TimeRecordRepository;
import example.domain.model.attendance.TimeRecords;
import example.domain.model.attendance.WorkMonth;
import example.domain.model.timerecord.evaluation.TimeRecord;
import example.domain.model.employee.Employee;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TimeRecordDataSource implements TimeRecordRepository {
    TimeRecordMapper mapper;

    @Override
    public void registerTimeRecord(TimeRecord timeRecord) {
        Integer identifier = mapper.newWorkTimeIdentifier();
        EmployeeNumber employeeNumber = timeRecord.employeeNumber();
        mapper.insertWorkTimeHistory(identifier, employeeNumber, timeRecord);
        mapper.deleteWorkTime(employeeNumber, timeRecord.actualWorkDateTime().workRange().start());
        mapper.insertWorkTime(employeeNumber, identifier, timeRecord);
    }

    @Override
    public TimeRecords findTimeRecords(Employee employee, WorkMonth month) {
        List<TimeRecord> timeRecords = mapper.selectByMonth(employee.employeeNumber(), month.yyyyMM());
        return new TimeRecords(timeRecords);
    }

    TimeRecordDataSource(TimeRecordMapper mapper) {
        this.mapper = mapper;
    }
}

@import com.example.demo.model.Transfer
@import java.util.List
@import java.time.LocalDate
@import java.time.format.DateTimeFormatter
@param List<Transfer> transfers

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Transfer List</title>
</head>
<body>
<h1>Football Transfers</h1>
<table border="1">
    <thead>
        <tr>
            <th>Player</th>
            <th>From Team</th>
            <th>To Team</th>
            <th>Date</th>
            <th>Fee</th>
        </tr>
    </thead>
    <tbody>
        @for(var transfer : transfers)
            <tr>
                <td>${transfer.getPlayer().getName()}</td>
                <td>${transfer.getFromTeam() != null ? transfer.getFromTeam().getName() : "Free Agent"}</td>
                <td>${transfer.getToTeam().getName()}</td>
                <td>${transfer.getTransferDate() != null ? transfer.getTransferDate().toString() : ""}</td>
                <td>${transfer.getTransferFee()}</td>
            </tr>
        @endfor
    </tbody>
</table>
</body>
</html>
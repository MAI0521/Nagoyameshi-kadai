//document.querySelector('input[type="datetime-local"]').addEventListener('focus', function() {
//    this.setAttribute('step', '1800');
//});


document.addEventListener("DOMContentLoaded", function() {
   flatpickr("#reservationDateTime", {
      enableTime: true,
      dateFormat: "Y-m-d H:i",
      time_24hr: true,
      minDate: "today",
      maxDate: new Date().fp_incr(14),
      minuteIncrement: 30,
   });
});
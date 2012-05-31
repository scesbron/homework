$(document).ready(function() {
  $('.with-tooltip').tooltip();
  $('.with-popover').popover({placement:'bottom'});
  $(':input:enabled:visible:first').focus();
});

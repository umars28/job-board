// Call the dataTables jQuery plugin
$(document).ready(function() {
    $('.select2-location').select2({
        placeholder: "Pilih Lokasi",
        allowClear: true,
        width: '100%'
    });

    $('.select2-category').select2({
        placeholder: "Pilih Kategori",
        allowClear: true,
        width: '100%'
    });

    $('.select2-tag').select2({
        placeholder: "Pilih Tag",
        allowClear: true,
        width: '100%',
        maximumSelectionLength: 3
    });


});

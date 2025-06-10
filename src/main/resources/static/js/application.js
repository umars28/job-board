// Call the dataTables jQuery plugin
$(document).ready(function() {
    $('#categoryDataTable').DataTable();
    $('#tagDataTable').DataTable();
    $('#seekerDataTable').DataTable();
    $('#companyDataTable').DataTable();
    $('#jobDataTable').DataTable();
    $('#jobApplicationTable').DataTable();

    // CATEGORY JOB MODULE
    $(document).on('click', '.btnEditCategory', function () {
        const id = $(this).data('id');
        const name = $(this).data('name');

        $('#editCategoryId').val(id);
        $('#editCategoryName').val(name);
        $('#editCategoryModal').modal('show');
    });

    const $categoryCreateModal = $('#createCategoryModal');
    const openCreateModal = $categoryCreateModal.data('open-create-modal') === true || $categoryCreateModal.data('open-create-modal') === 'true';

    if (openCreateModal) {
        $categoryCreateModal.modal('show');
    }

    $categoryCreateModal.find('.btn-secondary').click(function() {
        $categoryCreateModal.modal('hide');
    });


    const $editModal = $('#editCategoryModal');
    const openEditModal = $editModal.data('open-edit-modal') === true || $editModal.data('open-edit-modal') === 'true';

    if (openEditModal) {
        $editModal.modal('show');
    }
    // END CATEGORY JOB MODULE



    // TAG JOB MODULE
    $(document).on('click', '.btnEditTag', function () {
        const id = $(this).data('id');
        const name = $(this).data('name');
        const formAction = `/jobs/tag/update`;

        $('#editTagId').val(id);
        $('#name').val(name).focus();
        $('form').attr('action', formAction);
        $('.form-tag h6').text('Edit Tag');
        $('form button[type="submit"]').text('Update Tag');
    });

    $('#btnAddTag').on('click', function(e) {
        e.preventDefault();
        const formAction = `/jobs/tag/save`;
        const $form = $('form');
        $form[0].reset();

        $form.attr('action', formAction);
        $form.find('input[type="text"], input[type="hidden"]').val('');
        $('.form-tag h6').text('Create Tag');
        $('form button[type="submit"]').text('Create Tag');
        $('#name').focus();
    });
    //

});

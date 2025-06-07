// Call the dataTables jQuery plugin
$(document).ready(function() {
    $('#categoryDataTable').DataTable();

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


});

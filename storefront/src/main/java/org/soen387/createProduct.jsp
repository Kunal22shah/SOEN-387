<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Create Product</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>

<div class="container mt-5">
    <h2>Create New Product</h2>
    <div id="feedbackAlert" class="alert" role="alert" style="display:none;"></div>
    <form action="/createProduct" method="post">
        <div class="form-group">
            <label for="name">Product Name:</label>
            <input type="text" class="form-control" id="name" placeholder="Enter product name" name="name" required>
        </div>
        <div class="form-group">
            <label for="description">Description:</label>
            <textarea class="form-control" id="description" placeholder="Enter product description" name="description" required></textarea>
        </div>
        <div class="form-group">
            <label for="vendor">Vendor:</label>
            <input type="text" class="form-control" id="vendor" placeholder="Enter vendor name" name="vendor" required>
        </div>
        <div class="form-group">
            <label for="urlSlug">URL Slug:</label>
            <input type="text" class="form-control" id="urlSlug" placeholder="Enter URL slug" name="urlSlug" required>
        </div>
        <div class="form-group">
            <label for="sku">SKU:</label>
            <input type="text" class="form-control" id="sku" placeholder="Enter SKU" name="sku" required>
        </div>
        <div class="form-group">
            <label for="price">Price:</label>
            <input type="number" step="0.01" class="form-control" id="price" placeholder="Enter product price" name="price" required>
        </div>
        <button type="submit" class="btn btn-primary">Create Product</button>
    </form>
    <div id="loadingSpinner" class="text-center mt-3" style="display:none;">
        <div class="spinner-border text-primary" role="status">
            <span class="sr-only">Loading...</span>
        </div>
    </div>
</div>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
<script>
    $(document).ready(function() {
        $('#productForm').on('submit', function(e) {
            if (!validateForm()) {
                e.preventDefault(); 
                return;
            }
            
            $('#loadingSpinner').show();
        });

        function validateForm() {
    $('#feedbackAlert').hide().removeClass('alert-danger');

    if ($('#name').val().trim() === "") {
        showAlert('Product name is required.', 'danger');
        return false;
    }

    if ($('#description').val().trim() === "") {
        showAlert('Description is required.', 'danger');
        return false;
    }

    if ($('#vendor').val().trim() === "") {
        showAlert('Vendor name is required.', 'danger');
        return false;
    }

    if ($('#urlSlug').val().trim() === "") {
        showAlert('URL Slug is required.', 'danger');
        return false;
    } else if (!/^[0-9a-z-]+$/.test($('#urlSlug').val().trim())) {
        showAlert('URL Slug can only contain lowercase alphanumeric characters and hyphens.', 'danger');
        return false;
    }

    if ($('#sku').val().trim() === "") {
        showAlert('SKU is required.', 'danger');
        return false;
    }

    if ($('#price').val().trim() === "" || isNaN($('#price').val()) || parseFloat($('#price').val()) <= 0) {
        showAlert('Please enter a valid positive price.', 'danger');
        return false;
    }

    return true;
}

        function showAlert(message, type) {
            $('#feedbackAlert').text(message).addClass('alert-' + type).show();
        }
    });
</script>
</body>
</html>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="container-fluid">
    <div class="toolbar">
        <div class="row">
            <div class="col">
                <h2 class="page-title">スタッフを編集</h2>
            </div>
        </div>
    </div>

    <div class="card">
        <div class="card-body">
            <form action="${pageContext.request.contextPath}/dispatch?className=CoachServlet&methodName=update" method="post">
                <input type="hidden" name="id" value="${coach.id}">
                
                <div class="form-group">
                    <label>氏名</label>
                    <input type="text" name="name" class="form-control" value="${coach.name}" required>
                </div>
                
                <div class="form-group">
                    <label>性別</label>
                    <div>
                        <div class="form-check form-check-inline">
                            <input class="form-check-input" type="radio" name="gender" id="gender1" value="1" ${coach.gender == 1 ? 'checked' : ''}>
                            <label class="form-check-label" for="gender1">男性</label>
                        </div>
                        <div class="form-check form-check-inline">
                            <input class="form-check-input" type="radio" name="gender" id="gender0" value="0" ${coach.gender == 0 ? 'checked' : ''}>
                            <label class="form-check-label" for="gender0">女性</label>
                        </div>
                    </div>
                </div>
                
                <div class="form-group">
                    <label>電話番号</label>
                    <input 
                        type="text" 
                        name="phone" 
                        class="form-control" 
                        value="${coach.phone}" 
                        pattern="^[0-9-]{10,13}$" 
                        placeholder="例：09012345678"
                        required
                    >
                </div>
                
                <div class="form-group">
                    <label>メールアドレス</label>
                    <input type="email" name="email" class="form-control" value="${coach.email}" required>
                </div>
                
                <div class="form-group">
                    <label>プロフィール画像</label>
                    <div class="custom-file">
                        <input type="file" class="custom-file-input" id="avatarFile" accept="image/*">
                        <label class="custom-file-label" for="avatarFile">ファイルを選択</label>
                    </div>
                    <input type="hidden" name="avatar" id="avatarPath" value="${coach.avatar}">
                    <div id="avatarPreview" class="mt-2" style="display: ${empty coach.avatar ? 'none' : 'block'};">
                        <img src="${pageContext.request.contextPath}${coach.avatar}" 
                            class="img-thumbnail" 
                            style="max-width: 200px;">
                    </div>
                </div>
                
                <div class="form-group">
                    <label>担当レッスン</label>
                    <select name="equipmentId" class="form-control" required>
                        <option value="">レッスンを選択してください</option>
                        <c:forEach items="${equipments}" var="equipment">
                            <option value="${equipment.id}" ${coach.equipmentId == equipment.id ? 'selected' : ''}>${equipment.name}</option>
                        </c:forEach>
                    </select>
                </div>
                
                <div class="form-group">
                    <label>入社日</label>
                    <input type="date" name="hireDate" class="form-control" value="<fmt:formatDate value="${coach.hireDate}" pattern="yyyy-MM-dd"/>" required>
                </div>
                
                <div class="form-group">
                    <label>雇用状態</label>
                    <div>
                        <div class="form-check form-check-inline">
                            <input class="form-check-input" type="radio" name="status" id="status1" value="1" ${coach.status == 1 ? 'checked' : ''}>
                            <label class="form-check-label" for="status1">在職中</label>
                        </div>
                        <div class="form-check form-check-inline">
                            <input class="form-check-input" type="radio" name="status" id="status0" value="0" ${coach.status == 0 ? 'checked' : ''}>
                            <label class="form-check-label" for="status0">退職</label>
                        </div>
                    </div>
                </div>
                
                <div class="form-group">
                    <label>自己紹介</label>
                    <textarea name="description" class="form-control" rows="3" required>${coach.description}</textarea>
                </div>
                
                <div class="form-group">
                    <label>雇用形態</label>
                    <select name="type" class="form-control" required>
                        <option value="1" ${coach.type == 1 ? 'selected' : ''}>アルバイト</option>
                        <option value="2" ${coach.type == 2 ? 'selected' : ''}>正社員</option>
                    </select>
                </div>
                
                <div class="form-group">
                    <button type="submit" class="btn btn-primary">保存</button>
                    <a href="${pageContext.request.contextPath}/dispatch?className=CoachServlet&methodName=list" class="btn btn-secondary ml-2">戻る</a>
                </div>
            </form>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/libs/jquery.js"></script>
<script>
    // ファイル選択の処理
    $('#avatarFile').change(function() {
        const file = this.files[0];
        if (file) {
            // FormDataオブジェクトの作成
            const formData = new FormData();
            formData.append('file', file);
            
            // ファイルのアップロード
            $.ajax({
                url: '${pageContext.request.contextPath}/dispatch?className=ImageUploadServlet&methodName=upload',
                type: 'POST',
                data: formData,
                processData: false,
                contentType: false,
                success: function(result) {
                    if (result.success) {
                        // 画像パスの保存
                        $('#avatarPath').val(result.path);
                        // プレビューの表示
                        $('#avatarPreview img').attr('src', '${pageContext.request.contextPath}' + result.path);
                        $('#avatarPreview').show();
                        // ファイル名の表示を更新
                        $('.custom-file-label').text(file.name);
                    } else {
                        alert(result.message);
                    }
                }
            });
        }
    });
</script>
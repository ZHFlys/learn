<html lang="zh-CN">
<head>
    <meta charset="UTF-8"/>
    <title>文件管理器</title>
    <style>
        table {
            width: 50%;
            font-size: .938em;
            border-collapse: collapse;/*边框合并*/
        }
        th {
            text-align: left;
            padding: .5em .5em;
            font-weight: bold;
            background: #66677c;color: #fff;
        }

        td {
            padding: .5em .5em;
            border-bottom: solid 1px #ccc;
        }
        .link{
            background: #00d8fb;
            font-weight: bold;
        }
        table,table tr th, table tr td { border:1px solid #0094ff; }/*设置边框*/
    </style>
</head>
<body>
<table>
    <tr>
        <th>目录/文件</th>
        <th>文件路径</th>
        <th>文件父目录</th>
        <th>文件名</th>
        <th>文件大小</th>
    </tr>
    <tr>
        <td colspan="5" class="link"><a href="?fileParent=<#if oldPath??>${oldPath}<#else>6666cd76f96956469e7be39d750cc7d9</#if>">回到上一层目录</a></td>
    </tr>
    <#list allFile as file>
        <tr>
            <#if file.isDir>
                    <td class="link"><a href="?fileParent=${file.fileSub}&oldPath=${file.fileParent}">目录</a></td>
                <#else>
                    <td>文件</td>
            </#if>
            <td>${file.filePath}</td>
            <td>${file.fileParent}</td>
            <td>${file.fileName}</td>
            <td>${file.fileSize}</td>
        </tr>
    </#list>

    <tr>
        <td colspan="5" class="link"><a href="?fileParent=6666cd76f96956469e7be39d750cc7d9">回到主目录</a></td>
    </tr>
</table>
</body>
</html>
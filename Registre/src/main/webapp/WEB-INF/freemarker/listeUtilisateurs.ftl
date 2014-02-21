<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="fr" xml:lang="fr">
<head>
  <title>Liste des utilisateurs de Registre</title>
</head>
<body>
<h1>Les Utilisateurs</h1>
<table>
  <thead>
    <tr>
      <th>ID</th>
      <th>Nom</th>
      <th>Addresse email</th>
    </tr>
  </thead>
  <tbody>
    <#list utilisateurs as utilisateur>
      <tr>
      	<td>${utilisateur.id!"-"}</td>
      	<td>${utilisateur.nom}</td>
        <td>${utilisateur.email}</td>
      </tr>
    </#list>
  </tbody>
</table>
</body>
</html>

define([
  "collections/project",
  "views/projects"
], function (ProjectCollection, ProjectsView) {

  var module = {
    list: function () {
      var coll = new ProjectCollection
      var view = new ProjectsView({ collection: coll })
      coll.fetch()
    }
  }

  return module
})

package club.kreastol.kreastolcommunity.HelperClasses.Models;

public class PostItem {
        private String title;
        private String body;
        private String date;
        private String category;
        private String poster;

        public String getTitle() { return title; }

        public String getBody() { return body; }

        public String getDate() { return date; }

        public String getCategory() { return category; }

        public String getPoster() { return poster; }

        public PostItem (String title, String body, String date, String category, String poster){
                this.title = title;
                this.body = body;
                this.date = date;
                this.category = category;
                this.poster = poster;
        }
}

export class Question{

  id: string;
  title: string;
  content: string;
  tags: string[];


constructor(id: string, title: string, content: string, tags: string[]){
    this.id = id;
    this.title = title;
    this.content = content;
    this.tags = tags;
  }

}

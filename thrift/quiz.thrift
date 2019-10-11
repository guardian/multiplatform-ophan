namespace * ophan.thrift.quiz
namespace cocoa GLAOphanThriftQuiz

struct QuizProgressUpdate {
    /**
    * Total number of questions in the quiz.
    **/
    1: required i64 questions

    /**
    * Number of questions the user has answered in the quiz.
    **/
    2: required i64 answered
}

struct KnowledgeQuizResults {
    /**
    * Ordered sequence of indices of answers chosen to questions.
    **/
    1: required list<i32> answers

    /**
    * Number of correctly answered questions.
    **/
    2: required i64 score
}

struct PersonalityQuizResults {
    /**
    * Ordered sequence of indices of answers chosen to questions.
    **/
    1: required list<i32> answers

    /**
    * Index of personality bucket to which the user was assigned.
    **/
    2: required i64 bucket
}

struct QuizEvent {
    /**
    * Unique identifier for the quiz.
    **/
    1: required string quizId

    /**
    * Time into the quiz.
    **/
    2: required i64 elapsed

    3: optional QuizProgressUpdate quizProgressUpdate

    4: optional KnowledgeQuizResults knowledgeQuizResults

    5: optional PersonalityQuizResults personalityQuizResults
}
